package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.core.config.AppProperties;
import net.lovexq.seckill.core.support.activemq.MqProducer;
import net.lovexq.seckill.core.support.lianjia.CreateCallable;
import net.lovexq.seckill.core.support.lianjia.LianJiaParam;
import net.lovexq.seckill.core.support.lianjia.UpdateCallable;
import net.lovexq.seckill.kernel.dto.EstateItemDto;
import net.lovexq.seckill.kernel.model.CheckRecord;
import net.lovexq.seckill.kernel.model.EstateImage;
import net.lovexq.seckill.kernel.model.EstateItem;
import net.lovexq.seckill.kernel.repository.CheckRecordRepository;
import net.lovexq.seckill.kernel.repository.EstateImageRepository;
import net.lovexq.seckill.kernel.repository.EstateItemRepository;
import net.lovexq.seckill.kernel.repository.EstateItemSpecification;
import net.lovexq.seckill.kernel.service.EstateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.jms.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 房源业务层实现类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
@Service
public class EstateServiceImpl implements EstateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstateServiceImpl.class);
    @Autowired
    private EstateItemRepository estateItemRepository;
    @Autowired
    private EstateImageRepository estateImageRepository;
    @Autowired
    private CheckRecordRepository checkRecordRepository;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private MqProducer mqProducer;
    @Autowired
    private Queue createRecordQueue;
    @Autowired
    private Queue updateRecordQueue;

    @Override
    @Transactional
    public JsonResult invokeCrawler(String baseUrl, String region, Integer curPage, Integer totalPage) throws Exception {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(appProperties, baseUrl, region, curPage, totalPage);
            exec.submit(new CreateCallable(lianJiaParam, mqProducer, createRecordQueue));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            exec.shutdownNow();
            result = new JsonResult(500, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public JsonResult invokeUpdateCrawler(String batch, String baseUrl, String region, Integer curPage, Integer totalPage) throws Exception {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(appProperties, batch, baseUrl, region, curPage, totalPage);
            exec.submit(new UpdateCallable(lianJiaParam, mqProducer, updateRecordQueue));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            exec.shutdownNow();
            result = new JsonResult(500, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public void saveCrawlerData(byte[] dataArray) throws Exception {
        try {
            EstateItemDto dto = ProtoStuffUtil.deserialize(dataArray, EstateItemDto.class);
            // 先查看数据库是否已存在该记录
            EstateItem entity = estateItemRepository.findByHouseId(dto.getHouseId());
            if (entity != null) {
                BeanUtils.copyProperties(dto, entity, "id");
                // 先删除原有图片
                estateImageRepository.deleteByHouseCode(dto.getHouseId());
            } else {
                entity = new EstateItem();
                BeanUtils.copyProperties(dto, entity);
            }
            // 保存房源条目
            estateItemRepository.save(entity);
            // 保存房源图片
            saveImages(dto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 保存房源图片
     *
     * @param dto
     */
    private void saveImages(EstateItemDto dto) {
        List<EstateImage> imageList = dto.getEstateImageList();
        if (!CollectionUtils.isEmpty(imageList)) {
            for (EstateImage image : imageList) {
                // 普通图片
                if (image.getPictureId() != null) {
                    estateImageRepository.save(image);
                    // 户型图
                } else {
                    image.setPictureId(System.currentTimeMillis());
                    image.setHouseCode(dto.getHouseId());
                    image.setPictureType(0);
                    if (StringUtils.isBlank(image.getPictureSourceUrl())) {
                        image.setPictureSourceUrl(image.getUrl());
                    }
                    estateImageRepository.save(image);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateCrawlerData(byte[] dataArray) throws Exception {
        try {
            EstateItemDto dto = ProtoStuffUtil.deserialize(dataArray, EstateItemDto.class);
            String batch = dto.getBatch();
            String houseId = dto.getHouseId();
            CheckRecord checkRecord = checkRecordRepository.findByBatchAndHistoryCode(batch, houseId);
            if (checkRecord != null) {
                checkRecord.setStatus(1);
            } else {
                checkRecord = new CheckRecord(batch, houseId, 2);
            }
            checkRecord.setData(dataArray);
            checkRecordRepository.save(checkRecord);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstateItemDto> findForSaleList(Pageable pageable, Map<String, String> paramMap) throws Exception {
        Page<EstateItemDto> targetItemPage;
        List<EstateItemDto> targetItemList = new ArrayList<>();

        Page<EstateItem> sourceItemPage = estateItemRepository.findAll(EstateItemSpecification.getForSaleListSpec(paramMap), pageable);
        List<EstateItem> sourceItemList = sourceItemPage.getContent();

        if (CollectionUtils.isEmpty(sourceItemList)) {
            targetItemPage = new PageImpl<>(targetItemList);
            return targetItemPage;
        }

        for (EstateItem sourceItem : sourceItemList) {
            EstateItemDto targetItem = new EstateItemDto();
            BeanUtils.copyProperties(sourceItem, targetItem);

            targetItemList.add(targetItem);
        }

        targetItemPage = new PageImpl<>(targetItemList, pageable, sourceItemPage.getTotalElements());
        return targetItemPage;
    }

    @Override
    @Transactional(readOnly = true)
    public EstateItemDto findByHouseId(String id) throws Exception {
        EstateItemDto targetItem = new EstateItemDto();
        EstateItem sourceItem = estateItemRepository.findByHouseId(id);
        if (sourceItem != null) {
            BeanUtils.copyProperties(sourceItem, targetItem);

            EstateImage image = new EstateImage(null, id);
            Sort sort = new Sort(Sort.Direction.ASC, "pictureType");
            List<EstateImage> imageList = estateImageRepository.findAll(Example.of(image), sort);
            targetItem.setEstateImageList(imageList);
        }

        return targetItem;
    }

}