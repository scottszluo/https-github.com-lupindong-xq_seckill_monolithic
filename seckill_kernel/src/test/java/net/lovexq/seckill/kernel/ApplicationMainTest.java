package net.lovexq.seckill.kernel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 品冬 on 2017-4-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationMainTest {

    @Test
    public void genMillis() {
        System.out.println(System.currentTimeMillis());
    }
}