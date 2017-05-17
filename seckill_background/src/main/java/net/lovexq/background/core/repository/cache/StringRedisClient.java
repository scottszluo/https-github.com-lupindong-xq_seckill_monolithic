package net.lovexq.background.core.repository.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis客户端
 *
 * @author LuPindong
 * @time 2017-04-28 22:32
 */
@Component
public class StringRedisClient {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    //-------------------------------------------------------------------------------------------------

    //Key（键），简单的key-value操作

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     *
     * @param key
     * @return
     */
    public long ttl(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    /**
     * 实现命令：KEYS pattern，查找所有符合给定模式 pattern的 key
     */
    public Set<String> keys(String pattern) {
        return stringRedisTemplate.keys(pattern);
    }

    /**
     * 实现命令：DEL key，删除一个key
     *
     * @param key
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 实现命令：DEL keys，删除一批key
     *
     * @param key
     */
    public void del(Collection key) {
        stringRedisTemplate.delete(key);
    }

    //String（字符串）

    /**
     * 实现命令：SET key value，设置一个key-value（将字符串值 value关联到 key）
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 实现命令：SET key value EX seconds，设置key-value和超时时间（秒）
     *
     * @param key
     * @param value
     * @param timeout （以秒为单位）
     */
    public void set(String key, String value, long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 实现命令：GET key，返回 key所关联的字符串值。
     *
     * @param key
     * @return value
     */
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 实现命令：incr key，增加指定值
     *
     * @param key
     * @param value
     */
    public Long increment(String key, Long value) {
        return stringRedisTemplate.opsForValue().increment(key, value);
    }

    /**
     * 实现命令：incr key，增加指定值
     *
     * @param key
     * @param value
     */
    public Double increment(String key, Double value) {
        return stringRedisTemplate.opsForValue().increment(key, value);
    }

    //Hash（哈希表）

    /**
     * 实现命令：HSET key field value，将哈希表 key中的域 field的值设为 value
     *
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, Object value) {
        stringRedisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     *
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        return (String) stringRedisTemplate.opsForHash().get(key, field);
    }

    /**
     * 实现命令：HDEL key field [field ...]，删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     *
     * @param key
     * @param fields
     */
    public void hdel(String key, Object... fields) {
        stringRedisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 实现命令：HGETALL key，返回哈希表 key中，所有的域和值。
     *
     * @param key
     * @return
     */
    public Map<Object, Object> hgetall(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    //List（列表）

    /**
     * 实现命令：LLEN，返回列表 key 的长度
     *
     * @param key
     * @return 返回列表 key 的长度。
     */
    public long listLength(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * 实现命令：LPUSH key value，将一个值 value插入到列表 key的表头
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public long lpush(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 实现命令：LPOP key，移除并返回列表 key的头元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    public String lpop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * 实现命令：RPUSH key value，将一个值 value插入到列表 key的表尾(最右边)。
     *
     * @param key
     * @param value
     * @return 执行 LPUSH命令后，列表的长度。
     */
    public long rpush(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 实现命令：RPOP key，移除并返回列表 key的尾元素。
     *
     * @param key
     * @return 列表key的头元素。
     */
    public String rpop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    //Set（集合）

    /**
     * 实现命令：SADD key member，将一个 member元素加入到集合 key当中，已经存在于集合的 member元素将被忽略。
     *
     * @param key
     * @param member
     */
    public void sadd(String key, String member) {
        stringRedisTemplate.opsForSet().add(key, member);
    }

    /**
     * 实现命令：SMEMBERS key，返回集合 key 中的所有成员。
     *
     * @param key
     * @return
     */
    public Set<String> smemebers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    //SortedSet（有序集合）

    /**
     * 实现命令：ZADD key score member，将一个 member元素及其 score值加入到有序集 key当中。
     *
     * @param key
     * @param member
     * @param score
     */
    public void zadd(String key, String member, double score) {
        stringRedisTemplate.opsForZSet().add(key, member, score);
    }

    /**
     * 实现命令：ZRANGE key start stop，返回有序集 key中，指定区间内的成员。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 实现命令：ZREM key member [member ...] ，移除有序集合中的一个或多个成员。
     *
     * @param key
     * @param members
     * @return
     */
    public Long zrem(String key, String... members) {
        return stringRedisTemplate.opsForZSet().remove(key, members);
    }

    /**
     * 实现命令：	ZREMRANGEBYRANK key start stop，移除有序集合中给定的排名区间的所有成员。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zrem(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
    }
}