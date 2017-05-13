package net.lovexq.background.core.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import net.lovexq.background.core.properties.AppProperties;
import net.lovexq.background.core.repository.cache.RedisClient;
import net.lovexq.background.core.support.security.JwtClaims;
import net.lovexq.background.core.support.security.JwtTokenUtil;
import net.lovexq.seckill.common.exception.ApplicationException;
import net.lovexq.seckill.common.utils.CookieUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 认证过滤器
 *
 * @author LuPindong
 * @time 2017-05-07 10:32
 */
@Component
public class AuthenticationFilter implements Filter {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private AppProperties appProperties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if (uri.contains("special")) {

            Cookie tokenCookie = CookieUtil.getCookieByName(request, AppConstants.TOKEN);
            Cookie userNameCookie = CookieUtil.getCookieByName(request, AppConstants.USER_NAME);

            if (tokenCookie != null) {
                try {
                    // 请求的Token
                    String requestToken = tokenCookie.getValue();
                    Claims requestClaims = JwtTokenUtil.getClaims(requestToken, appProperties.getJwtSecretKey());
                    String requestAccount = requestClaims.getAudience();
                    String claimsUA = String.valueOf(requestClaims.get("userAgent"));
                    String requestUA = request.getHeader("User-Agent").toLowerCase();

                    // 检查是否失效
                    if (JwtTokenUtil.isTokenExpired(requestClaims) || !claimsUA.equals(requestUA)) {
                        throw new ApplicationException("登录已失效，请重新登录！");
                    }

                    // 缓存的Token
                    String redisToken = redisClient.getStrValue(requestAccount);
                    if (redisToken == null || !requestToken.equals(redisToken)) {
                        throw new ApplicationException("Redis中无此Token！");
                    }

                    // 当前日期往前退5分钟，如果最后有效期在其中，则可以更新Token，实现自动续期
                    Date expiration = requestClaims.getExpiration();
                    long currentTime = System.currentTimeMillis();
                    if (expiration.after(new Date(currentTime - 300)) && expiration.before(new Date(currentTime))) {
                        // 重新生成Token
                        requestClaims = new JwtClaims(claimsUA, requestAccount);
                        // 延迟有效时间
                        String token = JwtTokenUtil.generateToken(requestClaims, appProperties.getJwtExpiration(), appProperties.getJwtSecretKey());

                        // 更新Cookie
                        CookieUtil.createCookie(AppConstants.TOKEN, token, "127.0.0.1", appProperties.getJwtExpiration(), true, response);
                        CookieUtil.createCookie(AppConstants.USER_NAME, userNameCookie.getValue(), "127.0.0.1", appProperties.getJwtExpiration(), response);

                        // 缓存Token
                        redisClient.setStrValue(requestAccount, token, appProperties.getJwtExpiration());
                    }

                    request.setAttribute(AppConstants.CLAIMS, requestClaims);
                } catch (SignatureException e) {
                    throw new ApplicationException("非法请求，无效的Token！");
                } catch (JwtException e) {
                    throw new ApplicationException(e.getMessage(), e);
                } catch (ApplicationException e) {
                    throw new ApplicationException(e.getMessage(), e);
                }
                filterChain.doFilter(request, response);
            } else {
                throw new ApplicationException("受限内容，请登录后再操作！");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
