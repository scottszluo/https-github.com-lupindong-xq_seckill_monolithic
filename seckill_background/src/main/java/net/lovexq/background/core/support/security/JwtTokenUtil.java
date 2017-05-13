package net.lovexq.background.core.support.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtTokenUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * 生成Token
     *
     * @param claims
     * @param expiration
     * @param secretKey
     * @return
     */
    public static String generateToken(Claims claims, Long expiration, String secretKey) throws JwtException {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(expiration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public static Date generateExpirationDate(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从Token获取信息
     *
     * @param token
     * @param secretKey
     * @return
     */
    public static Claims getClaims(String token, String secretKey) throws JwtException {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从Token获取受众
     *
     * @param token
     * @param secretKey
     * @return
     */
    public static String getAudience(String token, String secretKey) throws JwtException {
        return getClaims(token, secretKey).getAudience();
    }

    /**
     * 从Token获取受众
     *
     * @param claims
     * @return
     */
    public static String getAudience(Claims claims) throws JwtException {
        return claims.getAudience();
    }

    /**
     * 有效期是否期满，过期为true
     *
     * @param token
     * @param secretKey
     * @return
     */
    public static Boolean isTokenExpired(String token, String secretKey) throws JwtException {
        Claims claims = getClaims(token, secretKey);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 有效期是否期满，过期为true
     *
     * @param claims
     * @return
     */
    public static Boolean isTokenExpired(Claims claims) throws JwtException {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

}