package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.kernel.model.SpecialStockModel;
import net.lovexq.seckill.kernel.repository.EstateItemRepository;
import net.lovexq.seckill.kernel.repository.SpecialStockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
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
    private SpecialStockRepository specialStockRepository;

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
            model.setSaleStatus(map.get("sale_status").toString());
            model.setCoverUrl(map.get("cover_url") != null ? map.get("cover_url").toString() : "");
            model.setNumber(random.nextInt(10) + 1);
            LocalDateTime sTime = LocalDateTime.now();
            LocalDateTime eTime = sTime.plusDays(3);
            model.setStartTime(sTime);
            model.setEndTime(eTime);
            specialStockRepository.save(model);
        }
    }
}