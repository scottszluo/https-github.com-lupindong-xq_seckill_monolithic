package net.lovexq.seckill.core.support.security;

import io.jsonwebtoken.Claims;
import org.junit.Test;

/**
 * Created by LuPindong on 2017-5-2.
 */
public class JwtTokenUtilTest {

    private Long expiration = 2592000L;
    private String jwtSecretKey = "3ab6886ccdd23fdd623fc470b53a156b!@#$%^&*";

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJsb3ZleHEubmV0IiwiaWF0IjoxNDkzNzE0Mzk1LCJzdWIiOiJ3d3cubG92ZXhxLm5ldCIsImF1ZCI6ImFkbWluMTIzIiwiZXhwIjoxNDk2MzA2Mzk1fQ.9JobGYUZJiW2RO6_8_L-Bcz-koObT2fyaKCwpBxBcE1_1HzLxozEUuaR1YGJBdNLiy8h1oilaf8TNy9NX8wdCg";

    // {iss=lovexq.net, iat=1493714395, sub=www.lovexq.net, aud=admin123, exp=1496306395}

    @Test
    public void testGenerateToken() {
        JwtClaims claims = new JwtClaims("admin123");
        String token = JwtTokenUtil.generateToken(claims, expiration, jwtSecretKey);
        System.out.println(token);
    }

    @Test
    public void testGetClaims() {
        Claims claims = JwtTokenUtil.getClaims(token, jwtSecretKey);
        System.out.println(claims);
    }

    @Test
    public void testGetAudience() {
        String account = JwtTokenUtil.getAudience(token, jwtSecretKey);
        System.out.println(account);
    }
}