package net.lovexq.background.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author LuPindong
 * @time 2017-04-28 22:51
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> template; // inject the template as ListOperations
    //至于这个为什么可以注入。需要参考AbstractBeanFactory doGetBean
    //super.setValue(((RedisOperations) value).opsForValue());就这一行代码  依靠一个editor
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> vOps;
    //@Autowired
    //private IncrDao incr;

    @Test
    public void testSet() {
        template.execute((RedisCallback<Boolean>) connection -> {
            byte[] key = "tempkey".getBytes();
            byte[] value = "tempvalue".getBytes();
            connection.set(key, value);
            return true;
        });
    }

    @Test
    public void testSet1() {
        vOps.set("tempkey", "tempvalue");
    }

    @Test
    public void addLink() {
        //System.out.println(incr.incr(13));
        //System.out.println(incr.get(13));
    }

}
