package net.lovexq.background.core.repository.cache;

import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
public class ByteRedisClient {

    @Autowired
    private RedisTemplate<String, Object> byteRedisTemplate;

    //Key（键），简单的key-value操作

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    public long ttl(String key) {
        return byteRedisTemplate.getExpire(key);
    }

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     */
    public Set<String> keys(String pattern) {
        return byteRedisTemplate.keys(pattern);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public void del(String key) {
        byteRedisTemplate.delete(key);
    }

    /**
     * 实现命令：DEL keys，删除一批key
     *
     * @param key
     */
    public void del(Collection key) {
        byteRedisTemplate.delete(key);
    }

    /**
     * 实现命令：LLEN，返回列表 key 的长度
     *
     * @param key
     * @return 返回列表 key 的长度。
     */
    public long listLength(String key) {
        return byteRedisTemplate.opsForList().size(key);
    }

    //使用ProtoStuff序列化/序列化值

    /**
     * 实现命令：SET key value，设置一个key-value（将值 value关联到 key）
     *
     * @param key
     * @param obj
     */
    public <T> void setByteObj(String key, T obj) {
        byte[] dataArray = ProtoStuffUtil.serialize(obj);
        byteRedisTemplate.opsForValue().set(key, dataArray);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param obj
     * @param timeout （以秒为单位）
     */
    public <T> void setByteObj(String key, T obj, long timeout) {
        byte[] dataArray = ProtoStuffUtil.serialize(obj);
        byteRedisTemplate.opsForValue().set(key, dataArray, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：SET key value，设置一个key-value（将值 value关联到 key）
     *
     * @param key
     * @param objList
     */
    public <T> void setByteList(String key, List<T> objList) {
        byte[] dataArray = ProtoStuffUtil.serializeList(objList);
        byteRedisTemplate.opsForValue().set(key, dataArray);
    }


    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param objList
     * @param timeout （以秒为单位）
     */
    public <T> void setByteList(String key, List<T> objList, long timeout) {
        byte[] dataArray = ProtoStuffUtil.serializeList(objList);
        byteRedisTemplate.opsForValue().set(key, dataArray, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：GET key，返回 key所关联的值。
     *
     * @param key
     * @return value
     */
    public <T> T getByteObj(String key, Class<T> targetClass) {
        Object data = byteRedisTemplate.opsForValue().get(key);
        if (data == null) return null;
        return ProtoStuffUtil.deserialize((byte[]) data, targetClass);
    }

    /**
     * 实现命令：GET key，返回 key所关联的值。
     *
     * @param key
     * @return value
     */
    public <T> List<T> getByteList(String key, Class<T> targetClass) {
        Object data = byteRedisTemplate.opsForValue().get(key);
        if (data == null) return null;
        return ProtoStuffUtil.deserializeList((byte[]) data, targetClass);
    }

    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     *
     * @param key
     * @param obj
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public <T> long lpushByte(String key, T obj) {
        byte[] dataArray = ProtoStuffUtil.serialize(obj);
        return byteRedisTemplate.opsForList().leftPush(key, dataArray);
    }

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    public <T> T lpopByte(String key, Class<T> targetClass) {
        Object data = byteRedisTemplate.opsForList().leftPop(key);
        if (data == null) return null;
        return ProtoStuffUtil.deserialize((byte[]) data, targetClass);
    }

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     *
     * @param key
     * @param obj
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public <T> long rpushByte(String key, T obj) {
        byte[] dataArray = ProtoStuffUtil.serialize(obj);
        return byteRedisTemplate.opsForList().rightPush(key, dataArray);
    }

    /**
     * 实现命令：RPOP key，移除并返回列表 key的尾元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    public <T> T rpopByte(String key, Class<T> targetClass) {
        Object data = byteRedisTemplate.opsForList().rightPop(key);
        if (data == null) return null;
        return ProtoStuffUtil.deserialize((byte[]) data, targetClass);
    }

    /**
     * 实现命令：ZADD key score member，将一个 member元素及其 score值加入到有序集 key当中。
     *
     * @param key
     * @param obj
     * @param score
     */
    public <T> Boolean zadd(String key, T obj, double score) {
        byte[] dataArray = ProtoStuffUtil.serialize(obj);
        return byteRedisTemplate.opsForZSet().add(key, dataArray, score);
    }

    /**
     * 实现命令：ZRANGE key start stop，返回有序集 key中，指定区间内的成员。
     *
     * @param key
     * @param targetClass
     * @param start
     * @param stop
     * @return
     */
    public <T> Set<T> zrange(String key, Class<T> targetClass, double start, double stop) {
        Object data = byteRedisTemplate.opsForZSet().rangeByScore(key, start, stop);
        if (data == null) return null;
        return ProtoStuffUtil.deserializeSet((byte[]) data, targetClass);
    }
}