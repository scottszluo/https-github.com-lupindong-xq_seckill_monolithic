package net.lovexq.background.special;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.core.repository.cache.StringRedisClient;
import net.lovexq.background.crawler.service.CrawlerService;
import net.lovexq.background.estate.model.EstateImageModel;
import net.lovexq.background.estate.model.EstateItemModel;
import net.lovexq.background.estate.repository.EstateImageRepository;
import net.lovexq.background.estate.repository.EstateItemRepository;
import net.lovexq.background.special.dto.SpecialStockDTO;
import net.lovexq.background.special.model.SpecialStockModel;
import net.lovexq.background.special.repository.SpecialStockRepository;
import net.lovexq.background.system.model.SystemConfigModel;
import net.lovexq.background.system.repository.SystemConfigRepository;
import net.lovexq.seckill.common.utils.*;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.common.utils.enums.EstateEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by LuPindong on 2017-4-26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecialTest {

    @Autowired
    private EstateItemRepository estateItemRepository;
    @Autowired
    private EstateImageRepository estateImageRepository;
    @Autowired
    private SpecialStockRepository specialStockRepository;
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private ByteRedisClient byteRedisClient;
    @Autowired
    private StringRedisClient stringRedisClient;

    private TemplateEngine templateEngine;

    @Before
    public void initTemplateEngine() {
        //构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

    }

    @Test
    public void clearCache() {
        String cacheKey = CacheKeyGenerator.generate(SpecialStockModel.class, "listForSecKill");
        byteRedisClient.del(cacheKey);

        List<String> keysList = new ArrayList();
        List<SpecialStockModel> list = specialStockRepository.findAll();
        for (SpecialStockModel model : list) {
            cacheKey = CacheKeyGenerator.generate(SpecialStockModel.class, "getByHouseCode", model.getHouseCode());
            keysList.add(cacheKey);
        }

        byteRedisClient.del(keysList);
    }

    @Test
    public void randomInsertSpecialStock() throws IllegalAccessException, InstantiationException {
        long targetId = System.currentTimeMillis() % 10000;
        List<Map> mapList = estateItemRepository.queryForMapList("select * from estate_item where id like '%" + targetId + "%' and cover_url is not null");
        System.out.println("本次生成的特价房源数：" + mapList.size());
        Random random = (new Random());
        LocalDate localDate = LocalDate.now();
        for (Map map : mapList) {
            SpecialStockModel model = new SpecialStockModel(IdWorker.INSTANCE.nextId());
            model.setBatch(localDate.toString() + "-" + mapList.size());
            model.setTitle(map.get("title").toString());
            model.setHouseCode(map.get("house_code").toString());
            model.setTotalPrice(new BigDecimal(map.get("total_price").toString()));
            model.setUnitPrice(new BigDecimal(map.get("unit_price").toString()));
            model.setModel(map.get("model").toString());
            model.setDirection(map.get("direction").toString());
            model.setArea(new BigDecimal(map.get("area").toString()));
            model.setFloor(map.get("floor").toString());
            model.setDecoration(map.get("decoration") != null ? map.get("decoration").toString() : "");
            model.setBuildingAge(map.get("building_age") != null ? map.get("building_age").toString() : "");
            model.setSaleState(map.get("sale_state").toString());
            model.setCoverUrl(map.get("cover_url") != null ? map.get("cover_url").toString() : "");
            model.setNumber(random.nextInt(10) + 1);
            LocalDateTime sTime = TimeUtil.nowDateTime();
            LocalDateTime eTime = sTime.plusDays(1);
            model.setStartTime(sTime);
            model.setEndTime(eTime);
            specialStockRepository.save(model);
        }
    }

    @Test
    public void randomInsertSpecialStockV2() throws IllegalAccessException, InstantiationException {
        // 先干掉原有数据
        clearCache();

        try {
            long targetId = System.currentTimeMillis() % 1000;
            List<EstateItemModel> estateItemList = estateItemRepository.findTop20ByHouseCodeLikeAndSaleState("%" + targetId + "%", EstateEnum.FOR_SALE.getValue());
            int maxNum = estateItemList.size();
            if (maxNum > estateItemList.size()) {
                maxNum = estateItemList.size();
            }

            // 更新特价批次
            SystemConfigModel sysConfigModel = systemConfigRepository.findByConfigKey("special_batch");
            if (sysConfigModel == null) {
                sysConfigModel = new SystemConfigModel();
                sysConfigModel.setConfigKey("special_batch");
                sysConfigModel.setConfigValue("0");
            }
            Long batch = Long.valueOf(sysConfigModel.getConfigValue()) + 1;
            sysConfigModel.setConfigValue(batch.toString());
            systemConfigRepository.saveAndFlush(sysConfigModel);

            System.out.println("本次生成的特价房源数：" + maxNum);
            LocalDate today = LocalDate.now();
            Random random = new Random();
            int count = 1;
            for (int i = 0; i < maxNum; i++) {
                EstateItemModel estateItem = estateItemList.get(i);
                SpecialStockModel old = specialStockRepository.findByHouseCode(estateItem.getHouseCode());
                if (old == null && EstateEnum.FOR_SALE.getValue().equals(estateItem.getSaleState())) {
                    // 插入特价库存表
                    SpecialStockModel specialStock = new SpecialStockModel(IdWorker.INSTANCE.nextId());
                    BeanUtils.copyProperties(estateItem, specialStock, "id");
                    specialStock.setTotal(random.nextInt(10) + 1);
                    specialStock.setNumber(specialStock.getTotal());
                    LocalDate.now().atStartOfDay();
                    LocalDateTime sTime = today.atStartOfDay().withHour(random.nextInt(24)).withMinute(0).withSecond(0);
                    LocalDateTime eTime = sTime.plusDays(1);
                    specialStock.setStartTime(sTime);
                    specialStock.setEndTime(eTime);
                    specialStock.setBatch(sysConfigModel.getConfigValue());
                    specialStockRepository.saveAndFlush(specialStock);

                    // 缓存全局库存计数器
                    stringRedisClient.increment(AppConstants.CACHE_SPECIAL_STOCK_COUNT + specialStock.getId(), specialStock.getTotal().longValue());

                    // 对选中房源标特价
                    estateItem.setSaleState(EstateEnum.SPECIAL.getValue());
                    estateItemRepository.save(estateItem);

                    // 生成静态页面
                    SpecialStockDTO specialStockDTO = new SpecialStockDTO();
                    CachedBeanCopier.copy(specialStock, specialStockDTO);

                    specialStockDTO.setStartTimeX(specialStockDTO.getStartTime().atZone(TimeUtil.shanghai).toInstant().toEpochMilli());
                    specialStockDTO.setEndTimeX(specialStockDTO.getEndTime().atZone(TimeUtil.shanghai).toInstant().toEpochMilli());

                    EstateImageModel condition = new EstateImageModel();
                    condition.setHouseCode(estateItem.getHouseCode());
                    Example<EstateImageModel> example = Example.of(condition);
                    List<EstateImageModel> estateImageModelList = estateImageRepository.findAll(example);
                    specialStockDTO.setEstateImageList(estateImageModelList);

                    // 转为Map
                    crawlerService.generateStaticPage(BeanMapUtil.beanToMap(specialStockDTO), "special_detailUI");

                    if (count++ > 20) break;
                }
            }


            String cacheKey = CacheKeyGenerator.generate(SystemConfigModel.class, "getByConfigKey", "special_batch");
            byteRedisClient.setByteObj(cacheKey, sysConfigModel);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        Assert.assertTrue(true);
    }

}