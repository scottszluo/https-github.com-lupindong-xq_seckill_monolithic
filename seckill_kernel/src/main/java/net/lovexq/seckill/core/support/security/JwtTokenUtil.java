package net.lovexq.seckill.core.support.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * 生成Token
     *
     * @param claims
     * @param expiration
     * @param secretKey
     * @return
     */
    public static String generateToken(Claims claims, Long expiration, String secretKey) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(expiration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    private static Date generateExpirationDate(Long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从Token获取信息
     *
     * @param token
     * @param secretKey
     * @return
     */
    public static Claims getClaims(String token, String secretKey) {
        Claims claims = new JwtClaims();
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return claims;
    }

    /**
     * 从Token获取受众
     *
     * @param token
     * @param secretKey
     * @return
     */
    public static String getAudience(String token, String secretKey) {
        String account = null;
        try {
            account = getClaims(token, secretKey).getAudience();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return account;
    }
}