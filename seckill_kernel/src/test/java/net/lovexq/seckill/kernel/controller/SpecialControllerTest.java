package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.common.utils.CacheKeyGenerator;
import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.kernel.model.EstateImageModel;
import net.lovexq.seckill.kernel.model.EstateItemModel;
import net.lovexq.seckill.kernel.model.SpecialStockModel;
import net.lovexq.seckill.kernel.repository.EstateImageRepository;
import net.lovexq.seckill.kernel.repository.EstateItemRepository;
import net.lovexq.seckill.kernel.repository.SpecialStockRepository;
import net.lovexq.seckill.kernel.service.impl.SpecialServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by LuPindong on 2017-4-26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpecialControllerTest {

    @Autowired
    private EstateItemRepository estateItemRepository;
    @Autowired
    private EstateImageRepository estateImageRepository;

    @Autowired
    private SpecialStockRepository specialStockRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void clearCache() {
        String key = CacheKeyGenerator.generate(SpecialServiceImpl.class, "listForSecKill");
        redisTemplate.delete(key);
    }

    @Test
    public void randomInsertSpecialStock() throws IllegalAccessException, InstantiationException {
        long targetId = System.currentTimeMillis() % 10000;
        List<Map> mapList = estateItemRepository.queryForMapList("select * from estate_item where id like '%" + targetId + "%' and cover_url is not null");
        System.out.println("本次生成的特价房源数：" + mapList.size());
        Random random = (new Random());
        for (Map map : mapList) {
            SpecialStockModel model = new SpecialStockModel(IdWorker.INSTANCE.nextId());
            model.setTitle(map.get("title").toString());
            model.setHouseId(map.get("house_id").toString());
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
            LocalDateTime sTime = LocalDateTime.now();
            LocalDateTime eTime = sTime.plusDays(3);
            model.setStartTime(sTime);
            model.setEndTime(eTime);
            specialStockRepository.save(model);
        }
    }

    @Test
    public void randomInsertSpecialStockV2() throws IllegalAccessException, InstantiationException {
        // 先干掉原有数据
        String key = CacheKeyGenerator.generate(SpecialServiceImpl.class, "listForSecKill");
        redisTemplate.delete(key);
        specialStockRepository.deleteAll();

        long targetId = System.currentTimeMillis() % 1000;
        List<EstateItemModel> estateItemList = estateItemRepository.findByHouseIdLike("%" + targetId + "%");
//        int maxNum = 15;
        int maxNum = estateItemList.size();
        if (maxNum > estateItemList.size()) {
            maxNum = estateItemList.size();
        }

        System.out.println("本次生成的特价房源数：" + maxNum);
        LocalDate today = LocalDate.now();
        Random random = new Random();
        for (int i = 0; i < maxNum; i++) {
            EstateItemModel estateIteml = estateItemList.get(i);
            SpecialStockModel specialStock = new SpecialStockModel(IdWorker.INSTANCE.nextId());
            BeanUtils.copyProperties(estateIteml, specialStock, "id");
            specialStock.setNumber(random.nextInt(10) + 1);
            LocalDate.now().atStartOfDay();
            LocalDateTime sTime = today.atStartOfDay().withHour(random.nextInt(24)).withMinute(0).withSecond(0);
            LocalDateTime eTime = sTime.plusDays(3);
            specialStock.setStartTime(sTime);
            specialStock.setEndTime(eTime);
            specialStockRepository.save(specialStock);
        }

    }

    @Test
    public void randomInsertEstate() throws IllegalAccessException, InstantiationException {
        List<EstateItemModel> estateItemList;
        EstateItemModel estateIteml;
        EstateItemModel newModel;

        EstateImageModel condition;
        List<EstateImageModel> imageModelList;
        EstateImageModel newImage;
        while (true) {
            long targetId = System.currentTimeMillis() % 1000;
            estateItemList = estateItemRepository.findByHouseIdLike("%" + targetId + "%");
            int maxNum = estateItemList.size();
            for (int i = 0; i < maxNum; i++) {
                estateIteml = estateItemList.get(i);
                if (estateIteml.getHouseId().contains("-N")) {
                    continue;
                }

                newModel = new EstateItemModel(IdWorker.INSTANCE.nextId());
                BeanUtils.copyProperties(estateIteml, newModel, "id");
                String postfix = "-N" + targetId + "-" + maxNum + "-" + i;
                newModel.setTitle(estateIteml.getTitle() + postfix);
                newModel.setHouseId(estateIteml.getHouseId() + postfix);
                LocalDateTime sTime = LocalDateTime.now();
                LocalDateTime eTime = sTime.plusDays(3);
                newModel.setCreateTime(sTime);
                newModel.setUpdateTime(eTime);
                estateItemRepository.saveAndFlush(newModel);

                // 相关图片
                condition = new EstateImageModel();
                condition.setHouseCode(estateIteml.getHouseId());
                imageModelList = estateImageRepository.findAll(Example.of(condition));
                for (EstateImageModel estateImageModel : imageModelList) {
                    newImage = new EstateImageModel();
                    BeanUtils.copyProperties(estateImageModel, newImage, "pictureId");
                    newImage.setHouseCode(newModel.getHouseId());
                    estateImageRepository.saveAndFlush(newImage);
                }
            }
        }
    }

}
