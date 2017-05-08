package net.lovexq.seckill.kernel;

import net.lovexq.background.special.model.SpecialStockModel;
import net.lovexq.background.special.repository.SpecialStockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by LuPindong on 2017-4-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationMainTest {

    @Autowired
    private SpecialStockRepository specialStockRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis() {
        SpecialStockModel model = specialStockRepository.findByHouseCode("GZ0002595190");
        SpecialStockModel mode2 = specialStockRepository.findByHouseCode("GZ0002429519");

        ValueOperations<String, SpecialStockModel> operations = redisTemplate.opsForValue();

        operations.set("model1", model);
        operations.set("mode2", mode2);

    }
    @Test
    public void testRedisV2() {
        //mylove
        ValueOperations<String,String> vp = redisTemplate.opsForValue();
        System.out.println(vp.getOperations());
    }
}