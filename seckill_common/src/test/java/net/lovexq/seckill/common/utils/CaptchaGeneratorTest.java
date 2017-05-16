package net.lovexq.seckill.common.utils;

import org.junit.Test;

/**
 * Created by 品冬 on 2017-5-15.
 */
public class CaptchaGeneratorTest {

    @Test
    public void testInit() {
        CaptchaGenerator.INSTANCE.genImage();
    }
}