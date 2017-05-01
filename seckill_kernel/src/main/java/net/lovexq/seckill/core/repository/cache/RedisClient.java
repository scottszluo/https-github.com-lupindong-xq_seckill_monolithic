package net.lovexq.seckill.core.repository.cache;

import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis客户端
 *
 * @author LuPindong
 * @time 2017-04-28 22:32
 */
@Component
public class RedisClient {

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    //Key（键），简单的key-value操作

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    //String（字符串）

    /**
     * 实现命令：SET key value，设置一个key-value（将值 value关联到 key）
     *
     * @param key
     * @param obj
     */
    public <T> void setObj(String key, T obj) {
        byte[] dataArray = ProtoStuffUtil.serialize(obj);
        redisTemplate.opsForValue().set(key, dataArray);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param obj
     * @param timeout （以秒为单位）
     */
    public <T> void setObj(String key, T obj, long timeout) {
        byte[] dataArray = ProtoStuffUtil.serialize(obj);
        redisTemplate.opsForValue().set(key, dataArray, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：SET key value，设置一个key-value（将值 value关联到 key）
     *
     * @param key
     * @param objList
     */
    public <T> void setList(String key, List<T> objList) {
        byte[] dataArray = ProtoStuffUtil.serializeList(objList);
        redisTemplate.opsForValue().set(key, dataArray);
    }


    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param objList
     * @param timeout （以秒为单位）
     */
    public <T> void setList(String key, List<T> objList, long timeout) {
        byte[] dataArray = ProtoStuffUtil.serializeList(objList);
        redisTemplate.opsForValue().set(key, dataArray, timeout, TimeUnit.SECONDS);
    }


    /**
     * 实现命令：GET key，返回 key所关联的值。
     *
     * @param key
     * @return value
     */
    public <T> T getObj(String key, Class<T> targetClass) {
        byte[] dataArray = redisTemplate.opsForValue().get(key);
        if (dataArray == null) {
            return null;
        }
        return ProtoStuffUtil.deserialize(dataArray, targetClass);
    }

    /**
     * 实现命令：GET key，返回 key所关联的值。
     *
     * @param key
     * @return value
     */
    public <T> List<T> getList(String key, Class<T> targetClass) {
        byte[] dataArray = redisTemplate.opsForValue().get(key);
        if (dataArray == null) {
            return null;
        }
        return ProtoStuffUtil.deserializeList(dataArray, targetClass);
    }
}