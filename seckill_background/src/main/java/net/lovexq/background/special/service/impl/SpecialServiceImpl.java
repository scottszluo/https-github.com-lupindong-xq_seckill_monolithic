package net.lovexq.background.special.service.impl;

import io.jsonwebtoken.Claims;
import net.lovexq.background.core.properties.AppProperties;
import net.lovexq.background.core.repository.cache.RedisClient;
import net.lovexq.background.estate.service.EstateService;
import net.lovexq.background.special.dto.SpecialStockDTO;
import net.lovexq.background.special.model.SpecialOrderModel;
import net.lovexq.background.special.model.SpecialStockModel;
import net.lovexq.background.special.repository.SpecialOrderRepository;
import net.lovexq.background.special.repository.SpecialStockRepository;
import net.lovexq.background.special.service.SpecialService;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.CacheKeyGenerator;
import net.lovexq.seckill.common.utils.CachedBeanCopier;
import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.common.utils.TimeUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private static final String INSERT_KILLED_SQL = "INSERT IGNORE INTO special_order(id,house_code,account,state,create_time,update_time) values(?,?,?,'下单',?,?)";
    @Autowired
    private SpecialStockRepository specialStockRepository;
    @Autowired
    private SpecialOrderRepository specialOrderRepository;
    @Autowired
    private EstateService estateService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private AppProperties appProperties;

    @Override
    @Transactional(readOnly = true)
    public List<SpecialStockDTO> listForSecKill() throws Exception {
        String cacheKey = CacheKeyGenerator.generate(SpecialStockModel.class, "listForSecKill");

        List<SpecialStockDTO> targetList = redisClient.getList(cacheKey, SpecialStockDTO.class);
        if (CollectionUtils.isNotEmpty(targetList)) {
            return targetList;
        } else {
            List<SpecialStockModel> sourceList = specialStockRepository.findForSecKillList();
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
                redisClient.setList(cacheKey, targetList, 60);
            }

            return targetList;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialStockDTO getOne(Long id) throws Exception {
        String cacheKey = CacheKeyGenerator.generate(SpecialStockModel.class, "getOne", id);

        SpecialStockDTO targetStock = redisClient.getObj(cacheKey, SpecialStockDTO.class);
        if (targetStock != null) {
            return targetStock;
        } else {
            targetStock = new SpecialStockDTO();
            SpecialStockModel sourceStock = specialStockRepository.getOne(id);
            if (sourceStock != null) {
                CachedBeanCopier.copy(sourceStock, targetStock);
                targetStock.setEstateImageList(estateService.listByHouseCode(targetStock.getHouseCode()));
                redisClient.setObj(cacheKey, targetStock, 60);
            }
            return targetStock;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JsonResult getExposureSecKillUrl(String houseCode, Claims claims) throws Exception {
        SpecialStockDTO specialStock = getOne(null);

        LocalDateTime startTime = specialStock.getStartTime(); // 秒杀开始时间
        LocalDateTime endTime = specialStock.getEndTime(); // 秒杀结束时间
        LocalDateTime nowTime = TimeUtil.nowDateTime(); //系统当前时间

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
            SpecialOrderModel order = specialOrderRepository.findByHouseCodeAndAccount(houseCode, account);
            if (order != null) {
                return new JsonResult(403, "请勿重复秒杀！");
            }
            return new JsonResult(getMd5Url(houseCode, account));
        }
    }

    private String getMd5Url(String houseCode, String account) throws UnsupportedEncodingException {
        String saltUrl = houseCode + "/" + account + "/" + appProperties.getPrivateSalt();
        return DigestUtils.md5DigestAsHex(saltUrl.getBytes(AppConstants.CHARSET_UTF8));
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
        String nowTime = TimeUtil.format(TimeUtil.nowDateTime());
        int insertCount = specialOrderRepository.executeUpdateBySql(INSERT_KILLED_SQL, IdWorker.INSTANCE.nextId(), houseCode, account, nowTime, nowTime);

        // 插入失败
        if (insertCount <= 0) {
            return new JsonResult(403, "请勿重复秒杀！");
            // 插入成功
        } else {
            // 执行减库存操作
            int updateCount = specialStockRepository.reduceNumber(houseCode, TimeUtil.nowDateTime());

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
    public void generateStaticPage(Long id) throws Exception {
        //构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //构造上下文(Model)
        Context context = new Context();
        context.setVariable("name", "蔬菜列表");
        context.setVariable("array", new String[]{"土豆", "番茄", "白菜", "芹菜"});

        //渲染模板
        FileWriter write = new FileWriter("result.html");
        templateEngine.process("example", context, write);
    }
}