package net.lovexq.background.core.properties;

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
    private String privateSalt;
    private String publicSalt;
    private String jwtSecretKey;
    private Long jwtExpiration;
    private String producesPath;

    public String getLiaJiaCookie() {
        return liaJiaCookie;
    }

    public void setLiaJiaCookie(String liaJiaCookie) {
        this.liaJiaCookie = liaJiaCookie;
    }

    public String getPrivateSalt() {
        return privateSalt;
    }

    public void setPrivateSalt(String privateSalt) {
        this.privateSalt = privateSalt;
    }

    public String getPublicSalt() {
        return publicSalt;
    }

    public void setPublicSalt(String publicSalt) {
        this.publicSalt = publicSalt;
    }

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public Long getJwtExpiration() {
        return jwtExpiration;
    }

    public void setJwtExpiration(Long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    public String getProducesPath() {
        return producesPath;
    }

    public void setProducesPath(String producesPath) {
        this.producesPath = producesPath;
    }
}