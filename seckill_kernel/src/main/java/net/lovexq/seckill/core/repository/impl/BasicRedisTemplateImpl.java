package net.lovexq.seckill.core.repository.impl;

import net.lovexq.seckill.core.repository.BasicRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis模板基类实现类
 *
 * @author LuPindong
 * @time 2017-04-28 22:32
 */
public class BasicRedisTemplateImpl implements BasicRedisTemplate {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

        public static <T> byte[] serialize(T obj) {

        return null;
    }

}