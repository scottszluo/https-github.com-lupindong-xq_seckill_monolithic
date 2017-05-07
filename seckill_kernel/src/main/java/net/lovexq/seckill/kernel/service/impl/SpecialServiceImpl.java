package net.lovexq.seckill.kernel.service.impl;

import io.jsonwebtoken.Claims;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.CacheKeyGenerator;
import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.core.properties.AppProperties;
import net.lovexq.seckill.core.repository.cache.RedisClient;
import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import net.lovexq.seckill.kernel.model.SpecialKilledModel;
import net.lovexq.seckill.kernel.model.SpecialStockModel;
import net.lovexq.seckill.kernel.repository.SpecialKilledRepository;
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
    private static final String INSERT_KILLED_SQL = "INSERT IGNORE INTO special_killed(id,house_code,account,state,create_time,update_time) values(?,?,?,'下单',now(),now())";
    @Autowired
    private SpecialStockRepository specialStockRepository;
    @Autowired
    private SpecialKilledRepository specialKilledRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private AppProperties appProperties;

    @Override
    @Transactional(readOnly = true)
    public List<SpecialStockModel> listForSecKill() throws Exception {
        String key = CacheKeyGenerator.generate(SpecialStockModel.class, "listForSecKill");

        List<SpecialStockModel> list = redisClient.getList(key, SpecialStockModel.class);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        } else {
            list = specialStockRepository.findBySaleStateOrderByStartTime("在售");
            if (!CollectionUtils.isEmpty(list)) {
                redisClient.setList(key, list, 20);
            }
            return list;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialStockDTO getByHouseCode(String houseCode) throws Exception {
        String key = CacheKeyGenerator.generate(SpecialStockModel.class, "getByHouseCode", houseCode);

        SpecialStockDTO targetStock = redisClient.getObj(key, SpecialStockDTO.class);
        if (targetStock != null) {
            return targetStock;
        } else {
            targetStock = new SpecialStockDTO();
            SpecialStockModel sourceStock = specialStockRepository.findByHouseCode(houseCode);
            if (sourceStock != null) {
                BeanUtils.copyProperties(sourceStock, targetStock);
                targetStock.setEstateImageList(imageService.listByHouseCode(houseCode));
                redisClient.setObj(key, targetStock, 10);
            }
            return targetStock;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JsonResult exposureSecKillUrl(String houseCode, Claims claims) throws Exception {
        SpecialStockDTO specialStock = getByHouseCode(houseCode);

        LocalDateTime startTime = specialStock.getStartTime(); // 秒杀开始时间
        LocalDateTime endTime = specialStock.getEndTime(); // 秒杀结束时间
        LocalDateTime nowTime = LocalDateTime.now(); //系统当前时间

        //若是秒杀未开始
        if (startTime.isAfter(nowTime)) {
            return new JsonResult(406, "秒杀活动未开始！");
            //秒杀已经结束
        } else if (endTime.isBefore(nowTime)) {
            return new JsonResult(406, "秒杀活动已经结束！");
            //秒杀处于开启窗口
        } else {
            String account = claims.getAudience();
            // 检查是否秒杀过
            SpecialKilledModel killed = specialKilledRepository.findByHouseCodeAndAccount(houseCode, account);
            if (killed != null) {
                return new JsonResult(403, "请勿重复秒杀！");
            }
            return new JsonResult(getMd5Url(houseCode, account));
        }
    }

    private String getMd5Url(String houseCode, String account) {
        String saltUrl = houseCode + "/" + account + "/" + appProperties.getPrivateSalt();
        return DigestUtils.md5DigestAsHex(saltUrl.getBytes());
    }

    @Override
    @Transactional
    public JsonResult executionSecKill(String houseCode, String key, Claims claims) throws Exception {
        JsonResult result = new JsonResult();

        String account = claims.getAudience();

        if (key == null || !key.equals(getMd5Url(houseCode, account))) {
            return new JsonResult(401, "秒杀请求无效！");
        }

        // 执行插入秒杀记录操作
        int insertCount = specialKilledRepository.executeUpdateBySql(INSERT_KILLED_SQL, IdWorker.INSTANCE.nextId(), houseCode, account);

        // 插入失败
        if (insertCount <= 0) {
            return new JsonResult(403, "请勿重复秒杀！");
            // 插入成功
        } else {
            // 执行减库存操作
            int updateCount = specialStockRepository.reduceNumber(houseCode, LocalDateTime.now());

            // 更新失败，说明库存为零，则秒杀失败
            if (updateCount <= 0) {
                return new JsonResult(410, "房源已售罄！");
                // 更新成功，则说明秒杀成功
            } else {
                result.setMessage("秒杀成功！");
            }
        }

        return result;
    }
}