package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.core.properties.AppProperties;
import net.lovexq.seckill.core.support.activemq.MqProducer;
import net.lovexq.seckill.core.support.lianjia.LianJiaCheckCallable;
import net.lovexq.seckill.core.support.lianjia.LianJiaInitializeCallable;
import net.lovexq.seckill.core.support.lianjia.LianJiaParam;
import net.lovexq.seckill.core.support.lianjia.LianJiaAddCallable;
import net.lovexq.seckill.kernel.dto.EstateItemDTO;
import net.lovexq.seckill.kernel.model.CrawlerRecordModel;
import net.lovexq.seckill.kernel.model.EstateImageModel;
import net.lovexq.seckill.kernel.model.EstateItemModel;
import net.lovexq.seckill.kernel.repository.CrawlerRecordRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
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
    private CrawlerRecordRepository crawlerRecordRepository;
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
    public JsonResult invokeCheck(String batch, String baseUrl, String region) {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            LianJiaParam lianJiaParam = new LianJiaParam(appProperties, batch, baseUrl, region);
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
    public JsonResult invokeUpdate(String batch, Integer curPage) {
        JsonResult result = new JsonResult();
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            List<EstateItemDTO> estateItemList = new ArrayList<>();
            List<CrawlerRecordModel> crawlerRecordList = crawlerRecordRepository.findByBatchAndStatusIn(batch, Arrays.asList(-1, 2));
            for (CrawlerRecordModel crawlerRecord : crawlerRecordList) {
                Integer status = crawlerRecord.getStatus();
                if (-1 == status) {
                    crawlerRecord.setStatus(0);
                    crawlerRecordRepository.save(crawlerRecord);
                    // 下架操作
                    estateItemRepository.updateStatus(crawlerRecord.getHistoryCode());
                } else {
                    // 新增操作
                    EstateItemDTO estateItemDTO = ProtoStuffUtil.deserialize(crawlerRecord.getData(), EstateItemDTO.class);
                    estateItemList.add(estateItemDTO);
                }
            }
            LianJiaParam lianJiaParam = new LianJiaParam(estateItemList, appProperties, batch, curPage);
            exec.submit(new LianJiaAddCallable(lianJiaParam, mqProducer, initializeQueue));
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
                    image.setPictureType(99);
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
            CrawlerRecordModel crawlerRecordModel = crawlerRecordRepository.findByBatchAndHistoryCode(batch, houseId);
            if (crawlerRecordModel != null) {
                crawlerRecordModel.setStatus(1);
            } else {
                crawlerRecordModel = new CrawlerRecordModel(batch, houseId, 2);
                List<CrawlerRecordModel> list = crawlerRecordRepository.findByBatchAndCurrentCode(batch, houseId);
                if (!CollectionUtils.isEmpty(list)) {
                    crawlerRecordRepository.deleteRepeatRecord(batch, houseId);
                }
            }
            crawlerRecordModel.setData(dataArray);
            crawlerRecordRepository.save(crawlerRecordModel);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}