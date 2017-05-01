package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.CacheKeyGenerator;
import net.lovexq.seckill.core.properties.AppProperties;
import net.lovexq.seckill.core.repository.cache.RedisClient;
import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import net.lovexq.seckill.kernel.model.SpecialStockModel;
import net.lovexq.seckill.kernel.repository.SpecialStockRepository;
import net.lovexq.seckill.kernel.service.ImageService;
import net.lovexq.seckill.kernel.service.SpecialService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 房源业务层实现类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
@Service
public class SpecialServiceImpl implements SpecialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialServiceImpl.class);
    @Autowired
    private SpecialStockRepository specialStockRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private AppProperties appProperties;

    @Override
    @Transactional(readOnly = true)
    public List<SpecialStockModel> listForSecKill() throws Exception {
        String key = CacheKeyGenerator.generate(this.getClass(), "listForSecKill");

        List<SpecialStockModel> list = redisClient.getList(key, SpecialStockModel.class);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        } else {
            list = specialStockRepository.findBySaleState("在售");
            if (!CollectionUtils.isEmpty(list)) {
                redisClient.setList(key, list);
            }
            return list;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialStockDTO getByHouseId(String id) throws Exception {
        String key = CacheKeyGenerator.generate(this.getClass(), "getByHouseId", id);

        SpecialStockDTO targetStock = redisClient.getObj(key, SpecialStockDTO.class);
        if (targetStock != null) {
            return targetStock;
        } else {
            targetStock = new SpecialStockDTO();
            SpecialStockModel sourceStock = specialStockRepository.findByHouseId(id);
            if (sourceStock != null) {
                BeanUtils.copyProperties(sourceStock, targetStock);
                targetStock.setEstateImageList(imageService.listByHouseId(id));
                redisClient.setObj(key, targetStock);
            }
            return targetStock;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JsonResult exposureSecKillUrl(String id) throws Exception {
        JsonResult result = new JsonResult();
        SpecialStockDTO specialStock = getByHouseId(id);

        LocalDateTime startTime = specialStock.getStartTime(); // 秒杀开始时间
        LocalDateTime endTime = specialStock.getEndTime(); // 秒杀结束时间
        LocalDateTime nowTime = LocalDateTime.now(); //系统当前时间

        //若是秒杀未开始，或者已经结束
        if (startTime.isAfter(nowTime) || endTime.isBefore(nowTime)) {
            result.setStatus(406);
            result.setMessage("秒杀未开始或已经结束！");
            return result;
            //秒杀开启
        } else {
            String saltUrl = id + "/" + appProperties.getPrivateSalt();
            String md5Url = DigestUtils.md5DigestAsHex(saltUrl.getBytes());
            result.setData(md5Url);
            return result;
        }
    }
}