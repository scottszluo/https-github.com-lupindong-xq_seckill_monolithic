package net.lovexq.seckill.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * APP自定义属性
 *
 * @author LuPindong
 * @time 2017-04-21 20:01
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String liaJiaCookie;
    private String salt;

    public String getLiaJiaCookie() {
        return liaJiaCookie;
    }

    public void setLiaJiaCookie(String liaJiaCookie) {
        this.liaJiaCookie = liaJiaCookie;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}