package net.lovexq.background.core;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.core.repository.cache.StringRedisClient;
import net.lovexq.background.special.model.SpecialOrderModel;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author LuPindong
 * @time 2017-04-28 22:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate; // inject the template as ListOperations
    //至于这个为什么可以注入。需要参考AbstractBeanFactory doGetBean
    //super.setValue(((RedisOperations) value).opsForValue());就这一行代码  依靠一个editor

    @Autowired
    private ByteRedisClient byteRedisClient;

    @Autowired
    private StringRedisClient stringRedisClient;

    @Test
    public void testZSet() {
        //Set<SpecialOrderModel> specialOrderModelSet = byteRedisClient.zrange(AppConstants.CACHE_ZSET_SPECIAL_ORDER + "864742020508422144", SpecialOrderModel.class, 0, -1);
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        ops.add("newKey", "12345x", 200);
        ops.add("newKey", "12345y", 300);
        ops.add("newKey", "12345z", 100);
        ops.add("newKey", "12345a", 400);
        ops.add("newKey", "12345b", 50);
        System.out.println(ops.range("newKey", 0, -1));
        ops.remove("newKey","12345a");
        System.out.println(ops.range("newKey", 0, -1));
    }

    @Test
    public void testZSet2() {
        Set<SpecialOrderModel> specialOrderModelSet = byteRedisClient.zrange(AppConstants.CACHE_ZSET_SPECIAL_ORDER + "864742020059631616", SpecialOrderModel.class, 0, -1);
        System.out.println(specialOrderModelSet.size());
    }

    @Test
    public void testList() {
        redisTemplate.opsForList().rightPush("pkey", "1");
        redisTemplate.opsForList().rightPush("pkey", "2");
        redisTemplate.opsForList().rightPush("pkey", "3");
//        System.out.println(template.opsForList().leftPop("pkey"));
//        System.out.println(template.opsForList().leftPop("pkey"));
//        System.out.println(template.opsForList().leftPop("pkey"));

        System.out.println(redisTemplate.opsForList().size("pkey"));
    }

    @Test
    public void testIncr() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().increment("ikey", 10L);
        System.out.println(redisTemplate.opsForValue().get("ikey"));
    }
}
