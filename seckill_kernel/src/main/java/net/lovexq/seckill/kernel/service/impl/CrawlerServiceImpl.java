package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.core.config.AppProperties;
import net.lovexq.seckill.core.support.activemq.MqProducer;
import net.lovexq.seckill.core.support.lianjia.LianJiaCheckCallable;
import net.lovexq.seckill.core.support.lianjia.LianJiaInitializeCallable;
import net.lovexq.seckill.core.support.lianjia.LianJiaParam;
import net.lovexq.seckill.kernel.dto.EstateItemDTO;
import net.lovexq.seckill.kernel.model.CheckRecordModel;
import net.lovexq.seckill.kernel.model.EstateImageModel;
import net.lovexq.seckill.kernel.model.EstateItemModel;
import net.lovexq.seckill.kernel.repository.CheckRecordRepository;
import net.lovexq.seckill.kernel.repository.EstateImageRepository;
import net.lovexq.seckill.kernel.repository.EstateItemRepository;
import net.lovexq.seckill.kernel.service.CrawlerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.jms.Queue;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LuPindong
 * @time 2017-04-27 08:12
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerServiceImpl.class);
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
    private Queue initializeQueue;
    @Autowired
    private Queue checkQueue;

    @Override
    public JsonResult invokeInitialize(String baseUrl, String region, Integer curPage, Integer totalPage) {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(appProperties, baseUrl, region, curPage, totalPage);
            exec.submit(new LianJiaInitializeCallable(lianJiaParam, mqProducer, initializeQueue));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            exec.shutdownNow();
            result = new JsonResult(500, e.getMessage());
        }
        return result;
    }

    @Override
    public JsonResult invokeCheck(String batch, String baseUrl, Integer curPage, Integer totalPage) {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(appProperties, batch, baseUrl, null, curPage, totalPage);
            exec.submit(new LianJiaCheckCallable(lianJiaParam, mqProducer, checkQueue));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            exec.shutdownNow();
            result = new JsonResult(500, e.getMessage());
        }
        return result;
    }


    @Override
    @Transactional
    public void saveInitializeData(byte[] dataArray) throws Exception {
        try {
            EstateItemDTO dto = ProtoStuffUtil.deserialize(dataArray, EstateItemDTO.class);
            // 先查看数据库是否已存在该记录
            EstateItemModel model = estateItemRepository.findByHouseId(dto.getHouseId());
            if (model != null) {
                BeanUtils.copyProperties(dto, model, "id");
                // 先删除原有图片
                estateImageRepository.deleteByHouseCode(dto.getHouseId());
            } else {
                model = new EstateItemModel();
                BeanUtils.copyProperties(dto, model);
            }
            // 保存房源条目
            estateItemRepository.save(model);
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
    private void saveImages(EstateItemDTO dto) {
        List<EstateImageModel> imageList = dto.getEstateImageList();
        if (!CollectionUtils.isEmpty(imageList)) {
            for (EstateImageModel image : imageList) {
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
    public void saveCheckData(byte[] dataArray) throws Exception {
        try {
            EstateItemDTO dto = ProtoStuffUtil.deserialize(dataArray, EstateItemDTO.class);
            String batch = dto.getBatch();
            String houseId = dto.getHouseId();
            CheckRecordModel checkRecordModel = checkRecordRepository.findByBatchAndHistoryCode(batch, houseId);
            if (checkRecordModel != null) {
                checkRecordModel.setStatus(1);
            } else {
                checkRecordModel = new CheckRecordModel(batch, houseId, 2);
                List<CheckRecordModel> list = checkRecordRepository.findByBatchAndCurrentCode(batch, houseId);
                if (!CollectionUtils.isEmpty(list)) {
                    checkRecordRepository.deleteRepeatRecord(batch, houseId);
                }
            }
            checkRecordModel.setData(dataArray);
            checkRecordRepository.save(checkRecordModel);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
