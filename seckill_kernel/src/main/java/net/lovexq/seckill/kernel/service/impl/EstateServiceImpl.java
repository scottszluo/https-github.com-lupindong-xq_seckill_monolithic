package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.core.config.AppProperties;
import net.lovexq.seckill.core.support.activemq.MqProducer;
import net.lovexq.seckill.core.support.lianjia.LianJiaCallable;
import net.lovexq.seckill.core.support.lianjia.LianJiaParam;
import net.lovexq.seckill.kernel.dto.EstateItemDto;
import net.lovexq.seckill.kernel.model.EstateImage;
import net.lovexq.seckill.kernel.model.EstateItem;
import net.lovexq.seckill.kernel.repository.EstateImageRepository;
import net.lovexq.seckill.kernel.repository.EstateItemRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 房产业务层实现类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
@Service
public class EstateServiceImpl implements EstateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstateServiceImpl.class);
    private static final String QUERY_COVER_URL_SQL = "SELECT url from estate_image WHERE picture_type = 1 and house_code = ? limit 1";
    @Autowired
    private EstateItemRepository estateItemRepository;
    @Autowired
    private EstateImageRepository estateImageRepository;
    @Autowired
    private MqProducer mqProducer;
    @Autowired
    private AppProperties appProperties;

    @Override
    @Transactional
    public JsonResult invokeCrawler(String baseUrl, String region, Integer curPage, Integer totalPage) throws Exception {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(mqProducer, appProperties, baseUrl, region, curPage, totalPage);
            exec.submit(new LianJiaCallable(lianJiaParam));
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
                estateItemRepository.save(entity);
            } else {
                entity = new EstateItem();
                BeanUtils.copyProperties(dto, entity);
                estateItemRepository.save(entity);

                List<EstateImage> imageList = dto.getEstateImageList();
                if (!CollectionUtils.isEmpty(imageList)) {
                    for (EstateImage image : imageList) {
                        if (StringUtils.isNotBlank(image.getPicture_id())) {
                            estateImageRepository.save(image);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstateItemDto> findForSaleList(Pageable pageable) {
        Page<EstateItemDto> targetItemPage;
        List<EstateItemDto> targetItemList = new ArrayList<>();

        EstateItem condition = new EstateItem();
        condition.setSaleStatus("在售");

        Page<EstateItem> sourceItemPage = estateItemRepository.findAll(Example.of(condition), pageable);
        List<EstateItem> sourceItemList = sourceItemPage.getContent();

        if (CollectionUtils.isEmpty(sourceItemList)) {
            targetItemPage = new PageImpl<>(targetItemList);
            return targetItemPage;
        }

        for (EstateItem sourceItem : sourceItemList) {
            EstateItemDto targetItem = new EstateItemDto();
            BeanUtils.copyProperties(sourceItem, targetItem);
            // 获取默认封面链接
            String url = String.valueOf(estateImageRepository.queryForObject(QUERY_COVER_URL_SQL, targetItem.getHouseId()));
            if (StringUtils.isNotBlank(url) && !"null".equals(url)) {
                targetItem.setCoverUrl(url);
            }
            targetItemList.add(targetItem);
        }

        targetItemPage = new PageImpl<>(targetItemList, pageable, sourceItemPage.getTotalElements());
        return targetItemPage;
    }

    @Override
    public EstateItemDto findByHouseId(String id) {
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