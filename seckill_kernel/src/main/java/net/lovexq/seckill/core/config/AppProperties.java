package net.lovexq.seckill.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 链家相关属性
 *
 * @author LuPindong
 * @time 2017-04-21 20:01
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String liaJiaCookie;

    public String getLiaJiaCookie() {
        return liaJiaCookie;
    }

    public void setLiaJiaCookie(String liaJiaCookie) {
        this.liaJiaCookie = liaJiaCookie;
    }
}
