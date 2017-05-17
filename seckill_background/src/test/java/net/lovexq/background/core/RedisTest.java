package net.lovexq.background.core;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

    @Test
    public void testSet() {
        redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            byte[] key = "tempkey".getBytes();
            byte[] value = "tempvalue".getBytes();
            connection.set(key, value);
            return true;
        });
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
