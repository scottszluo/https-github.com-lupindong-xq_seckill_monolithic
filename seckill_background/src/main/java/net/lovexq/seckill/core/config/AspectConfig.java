package net.lovexq.seckill.core.config;

import net.lovexq.seckill.core.support.dynamicds.DynamicDataSourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Aspect切面配置
 *
 * @author LuPindong
 * @time 2017-04-27 20:44
 */
@EnableAspectJAutoProxy
@Configuration
public class AspectConfig {

    @Bean
    public DynamicDataSourceAspect rdsAspect() {
        return new DynamicDataSourceAspect();
    }
}