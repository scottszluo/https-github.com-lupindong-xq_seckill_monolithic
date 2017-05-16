package net.lovexq.background.core.support.security;

import io.jsonwebtoken.Claims;
import org.junit.Test;

import java.util.Date;

/**
 * Created by LuPindong on 2017-5-2.
 */
public class JwtTokenUtilTest {

    private Long expiration = 3600L;
    private String jwtSecretKey = "3ab6886ccdd23fdd623fc470b53a156b!@#$%^&*";
    private String userAgent = "mozilla/5.0 (windows nt 6.3; wow64) applewebkit/537.36 (khtml, like gecko) chrome/57.0.2987.133 safari/537.36";
    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJsb3ZleHEubmV0IiwiaWF0IjoxNDk0Njc4NzkwLCJzdWIiOiJ3d3cubG92ZXhxLm5ldCIsImF1ZCI6ImFkbWluMTIzIiwidXNlckFnZW50IjoibW96aWxsYS81LjAgKHdpbmRvd3MgbnQgNi4zOyB3b3c2NCkgYXBwbGV3ZWJraXQvNTM3LjM2IChraHRtbCwgbGlrZSBnZWNrbykgY2hyb21lLzU3LjAuMjk4Ny4xMzMgc2FmYXJpLzUzNy4zNiIsImV4cCI6MTQ5NzI3MDc5MH0.2rNuVMrchtwGv5BqAD8wmk4D3TgNtwqbZsZYoHCwphO6BLfqYG4Ngt18tGiyuNIYz4VhXppWwKHtwLra2-k8Fw";

    // {iss=lovexq.net, iat=1493714395, sub=www.lovexq.net, aud=admin123, exp=1496306395}

    @Test
    public void testGenerateToken() {
        JwtClaims claims = new JwtClaims("admin123",userAgent,"admin123x");
        String token = JwtTokenUtil.generateToken(claims, expiration, jwtSecretKey);
        System.out.println(token);

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        System.out.println("issuedAt:" + issuedAt + ",expiration:" + expiration);
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