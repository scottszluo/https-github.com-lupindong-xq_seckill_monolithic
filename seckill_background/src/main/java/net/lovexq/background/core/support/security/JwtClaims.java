package net.lovexq.background.core.support.security;

import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Date;
import java.util.Objects;

/**
 * @author LuPindong
 * @time 2017-05-02 16;01
 */
public class JwtClaims extends DefaultClaims {

    private String userAgent; // 用户设备
    private String userName; // 用户名称

    public JwtClaims() {
        super();
    }

    public JwtClaims(String audience) {
        super();
        super.setIssuer("lovexq.net");
        super.setIssuedAt(new Date());
        super.setSubject("www.lovexq.net");
        super.setAudience(audience);
    }

    public JwtClaims(String audience, String userAgent) {
        super();
        super.setIssuer("lovexq.net");
        super.setIssuedAt(new Date());
        super.setSubject("www.lovexq.net");
        super.setAudience(audience);
        super.setValue("userAgent", userAgent);
    }

    public JwtClaims(String audience, String userAgent, String userName) {
        super();
        super.setIssuer("lovexq.net");
        super.setIssuedAt(new Date());
        super.setSubject("www.lovexq.net");
        super.setAudience(audience);
        super.setValue("userAgent", userAgent);
        super.setValue("userName", userName);
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtClaims)) return false;
        if (!super.equals(o)) return false;
        JwtClaims jwtClaims = (JwtClaims) o;
        return Objects.equals(userAgent, jwtClaims.userAgent) &&
                Objects.equals(userName, jwtClaims.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userAgent, userName);
    }

}
