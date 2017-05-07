package net.lovexq.seckill.core.support.security;

import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Date;

/**
 * @author LuPindong
 * @time 2017-05-02 16;01
 */
public class JwtClaims extends DefaultClaims {

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

}
