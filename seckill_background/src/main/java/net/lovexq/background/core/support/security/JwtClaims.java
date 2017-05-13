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

    public JwtClaims() {
        super();
    }

    public JwtClaims(String aud) {
        super();
        super.setIssuer("lovexq.net");
        super.setIssuedAt(new Date());
        super.setSubject("www.lovexq.net");
        super.setAudience(aud);
    }

    public JwtClaims(String userAgent, String aud) {
        super();
        super.setIssuer("lovexq.net");
        super.setIssuedAt(new Date());
        super.setSubject("www.lovexq.net");
        super.setAudience(aud);
        super.setValue("userAgent", userAgent);
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JwtClaims)) return false;
        if (!super.equals(o)) return false;
        JwtClaims jwtClaims = (JwtClaims) o;
        return Objects.equals(userAgent, jwtClaims.userAgent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userAgent);
    }
}
