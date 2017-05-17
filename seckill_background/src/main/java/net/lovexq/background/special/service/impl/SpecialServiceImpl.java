package net.lovexq.background.special.service.impl;

import io.jsonwebtoken.Claims;
import net.lovexq.background.core.properties.AppProperties;
import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.core.repository.cache.StringRedisClient;
import net.lovexq.background.crawler.service.CrawlerService;
import net.lovexq.background.estate.model.EstateItemModel;
import net.lovexq.background.estate.service.EstateService;
import net.lovexq.background.special.dto.SpecialStockDTO;
import net.lovexq.background.special.model.SpecialOrderModel;
import net.lovexq.background.special.model.SpecialStockModel;
import net.lovexq.background.special.repository.SpecialOrderRepository;
import net.lovexq.background.special.repository.SpecialStockRepository;
import net.lovexq.background.special.service.SpecialService;
import net.lovexq.background.system.model.SystemConfigModel;
import net.lovexq.background.system.repository.SystemConfigRepository;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.*;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.common.utils.enums.EstateEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 房源业务层实现类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
@Service
public class SpecialServiceImpl implements SpecialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialServiceImpl.class);
    private static final String INSERT_KILLED_SQL = "INSERT IGNORE INTO special_order(id,stock_id,account,state,create_time,update_time) values(?,?,?,'下单',?,?)";
    @Autowired
    private EstateService estateService;
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private SpecialStockRepository specialStockRepository;
    @Autowired
    private SpecialOrderRepository specialOrderRepository;
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private ByteRedisClient byteRedisClient;
    @Autowired
    private StringRedisClient stringRedisClient;
    @Autowired
    private AppProperties appProperties;

    @Override
    @Transactional
    public JsonResult createData() throws Exception {
        JsonResult result = new JsonResult();

        LocalDateTime nowDateTime = TimeUtil.nowDateTime();
        String targetCode = "%" + nowDateTime.getMinute() + "%";
        List<EstateItemModel> estateItemModelList = estateService.findTop20ByHouseCodeLikeAndSaleState(targetCode, EstateEnum.FOR_SALE.getValue());
        if (CollectionUtils.isNotEmpty(estateItemModelList)) {
            // 先清空列表数据缓存
            String cacheKeyForList = CacheKeyGenerator.generate(SpecialStockModel.class, "listForSecKill");
            byteRedisClient.del(cacheKeyForList);

            // 更新特价批次
            SystemConfigModel sysConfigModel = systemConfigRepository.findByConfigKey("special_batch");
            if (sysConfigModel == null) {
                sysConfigModel = new SystemConfigModel();
                sysConfigModel.setConfigKey("special_batch");
                sysConfigModel.setConfigValue("0");
            }
            Long batch = Long.valueOf(sysConfigModel.getConfigValue()) + 1;
            sysConfigModel.setConfigValue(batch.toString());
            systemConfigRepository.save(sysConfigModel);
            String cacheKey = CacheKeyGenerator.generate(SystemConfigModel.class, "getByConfigKey", "special_batch");
            byteRedisClient.setByteObj(cacheKey, sysConfigModel);

            // 开始生成特价库存相关
            Random random = new Random();
            for (EstateItemModel estateItem : estateItemModelList) {
                // 插入特价库存表
                SpecialStockModel specialStock = new SpecialStockModel(IdWorker.INSTANCE.nextId());
                BeanUtils.copyProperties(estateItem, specialStock, "id");
                specialStock.setTotal(random.nextInt(10) + 1);
                specialStock.setNumber(specialStock.getTotal());
                //LocalDateTime sTime = nowDateTime.toLocalDate().atStartOfDay().withHour(random.nextInt(24)).plusHours(nowDateTime.getHour()).withMinute(0).withSecond(0);
                LocalDateTime sTime = nowDateTime.minusHours(2).plusHours(random.nextInt(6)).withMinute(0).withSecond(0);
                LocalDateTime eTime = sTime.plusHours(3);
                specialStock.setStartTime(sTime);
                specialStock.setEndTime(eTime);
                specialStock.setBatch(sysConfigModel.getConfigValue());
                specialStockRepository.save(specialStock);

                // 缓存全局库存计数器
                stringRedisClient.increment(AppConstants.CACHE_SPECIAL_STOCK_COUNT + specialStock.getId(), specialStock.getTotal().longValue());

                // 对选中房源标特价
                estateItem.setSaleState(EstateEnum.SPECIAL.getValue());
                estateService.save(estateItem);

                // 生成静态页面
                SpecialStockDTO specialStockDTO = new SpecialStockDTO();
                CachedBeanCopier.copy(specialStock, specialStockDTO);

                specialStockDTO.setStartTimeX(specialStockDTO.getStartTime().atZone(TimeUtil.shanghai).toInstant().toEpochMilli());
                specialStockDTO.setEndTimeX(specialStockDTO.getEndTime().atZone(TimeUtil.shanghai).toInstant().toEpochMilli());
                specialStockDTO.setEstateImageList(estateService.listByHouseCode(estateItem.getHouseCode()));

                // 转为Map
                crawlerService.generateStaticPage(BeanMapUtil.beanToMap(specialStockDTO), "special_detailUI");
            }

        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpecialStockDTO> listForSecKill() throws Exception {
        String cacheKey = CacheKeyGenerator.generate(SpecialStockModel.class, "listForSecKill");

        List<SpecialStockDTO> targetList = byteRedisClient.getByteList(cacheKey, SpecialStockDTO.class);
        if (CollectionUtils.isNotEmpty(targetList)) {
            return targetList;
        } else {
            List<SpecialStockModel> sourceList = specialStockRepository.findForSecKillList(TimeUtil.format(TimeUtil.nowDateTime()));
            targetList = new ArrayList();
            if (CollectionUtils.isNotEmpty(sourceList)) {
                for (SpecialStockModel source : sourceList) {
                    SpecialStockDTO target = new SpecialStockDTO();
                    CachedBeanCopier.copy(source, target);

                    target.setDetailHref("/special/" + target.getHouseCode() + ".shtml");
                    target.setTotalPriceOriginal("<del>原价" + target.getTotalPrice() + "万</del>");
                    target.setTotalPriceCurrent("秒杀价" + BigDecimal.valueOf(0.1d).multiply(target.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万");
                    target.setUnitPriceStr("单价" + target.getUnitPrice() + "万");
                    target.setAreaStr(target.getArea() + "平米");

                    targetList.add(target);
                }
                byteRedisClient.setByteList(cacheKey, targetList, 60);
            }

            return targetList;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialStockDTO getOne(Long id) throws Exception {
        String cacheKey = CacheKeyGenerator.generate(SpecialStockModel.class, "getOne", id);

        SpecialStockDTO targetStock = byteRedisClient.getByteObj(cacheKey, SpecialStockDTO.class);
        if (targetStock != null) {
            return targetStock;
        } else {
            targetStock = new SpecialStockDTO();
            SpecialStockModel sourceStock = specialStockRepository.getOne(id);
            if (sourceStock != null) {
                CachedBeanCopier.copy(sourceStock, targetStock);
                targetStock.setEstateImageList(estateService.listByHouseCode(targetStock.getHouseCode()));
                byteRedisClient.setByteObj(cacheKey, targetStock, 60);
            }
            return targetStock;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JsonResult exposureSecKillUrl(Long id, Claims claims) throws Exception {
        String account = claims.getAudience();


        SpecialStockDTO specialStock = getOne(id);

        LocalDateTime startTime = specialStock.getStartTime(); // 秒杀开始时间
        LocalDateTime endTime = specialStock.getEndTime(); // 秒杀结束时间
        LocalDateTime nowTime = TimeUtil.nowDateTime(); //系统当前时间

        //若是秒杀未开始
        if (startTime.isAfter(nowTime)) {
            return new JsonResult(406, "秒杀活动未开始！");
            //秒杀已经结束
        } else if (endTime.isBefore(nowTime) || specialStock.getNumber() < 1) {
            return new JsonResult(406, "秒杀活动已经结束！");
            //秒杀处于开启窗口
        } else {
            // 检查是否秒杀过
            SpecialOrderModel order = specialOrderRepository.findByStockIdAndAccount(id, account);
            if (order != null) {
                return new JsonResult(403, "已秒杀成功，请勿重复秒杀！");
            }
            return new JsonResult(getMd5Url(id));
        }
    }

    private String getMd5Url(Long id) throws UnsupportedEncodingException {
        String cacheKey = CacheKeyGenerator.generate(String.class, "getMd5Url", id);
        // 先从缓存中取
        String md5Url = byteRedisClient.getByteObj(cacheKey, String.class);
        if (StringUtils.isBlank(md5Url)) {
            String saltUrl = id + "/" + Instant.now().toEpochMilli() + "/" + appProperties.getPrivateSalt();
            md5Url = DigestUtils.md5DigestAsHex(saltUrl.getBytes(AppConstants.CHARSET_UTF8));
            byteRedisClient.setByteObj(cacheKey, md5Url, 3600);
        }
        return md5Url;
    }

    @Override
    public JsonResult executeSecKill(Long id, String key, String captcha, Claims claims) throws Exception {
        JsonResult result = new JsonResult();

        String account = claims.getAudience();

        // 先检查验证码
        String cacheKey = AppConstants.CACHE_CAPTCHA + account;
        String cacheCaptcha = byteRedisClient.getByteObj(cacheKey, String.class);
        if (StringUtils.isBlank(cacheCaptcha)) {
            return new JsonResult(401, "验证码已过期，请刷新重试！");
        }
        if (!captcha.equals(cacheCaptcha)) {
            return new JsonResult(401, "请输入正确的验证码！");
        }

        if (key == null || !key.equals(getMd5Url(id))) {
            return new JsonResult(401, "秒杀请求无效！");
        }

        SpecialOrderModel specialOrderModel = new SpecialOrderModel(id, account, "下单");

        // 加到缓存队列
        Boolean isAdd = byteRedisClient.zadd(AppConstants.CACHE_ZSET_SPECIAL_ORDER + id, specialOrderModel, Instant.now().toEpochMilli());
        if (isAdd) {
            // 成功加入才减库存
            Long stock = stringRedisClient.increment(AppConstants.CACHE_SPECIAL_STOCK_COUNT + id, -1L);
            if (stock < 0) {
                return new JsonResult(410, "房源已售罄！");
            }
        } else {
            return new JsonResult(403, "已秒杀成功，请勿重复秒杀！");
        }

        return result;
    }

    @Override
    @Transactional
    public JsonResult decreaseStock(SpecialOrderModel specialOrderModel) throws Exception {
        JsonResult result = new JsonResult();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始对[用户账户: " + specialOrderModel.getAccount() + "], [房源ID:" + specialOrderModel.getStockId() + "]的特价秒杀订单记录进行减库存操作");
        }

        String nowTime = TimeUtil.format(specialOrderModel.getCreateTime());
        int insertCount = specialOrderRepository.executeUpdateBySql(INSERT_KILLED_SQL, IdWorker.INSTANCE.nextId(), specialOrderModel.getStockId(), specialOrderModel.getAccount(), nowTime, nowTime);
        // 插入失败
        if (insertCount <= 0) {
            return new JsonResult(403, "请勿重复秒杀！");
            // 插入成功
        } else {
            // 执行减库存操作
            int updateCount = specialStockRepository.reduceNumber(specialOrderModel.getStockId(), TimeUtil.nowDateTime());
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

    @Override
    @Transactional(readOnly = true)
    public void saveCaptcha(Claims claims, String captcha) {
        String cacheKey = AppConstants.CACHE_CAPTCHA + claims.getAudience();
        byteRedisClient.setByteObj(cacheKey, captcha, 120);
    }

    @Override
    public List<SpecialStockModel> listReadyStock(String startTime, boolean isAll) {
        if (isAll) {
            return specialStockRepository.findAllReadyStockList(startTime);
        } else {
            return specialStockRepository.findReadyStockList(startTime);
        }
    }

}