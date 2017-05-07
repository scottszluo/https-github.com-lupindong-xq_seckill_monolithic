package net.lovexq.seckill.core.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import net.lovexq.seckill.common.exception.ApplicationException;
import net.lovexq.seckill.common.utils.CookieUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.core.properties.AppProperties;
import net.lovexq.seckill.core.support.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过滤器
 *
 * @author LuPindong
 * @time 2017-05-07 10:32
 */
@Component
public class AuthenticationFilter implements Filter {

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
        if (uri.contains("execution")) {
            Cookie tokenCookie = CookieUtil.getCookieByName(request, AppConstants.TOKEN);
            Cookie userNameCookie = CookieUtil.getCookieByName(request, AppConstants.USER_NAME);

            if (tokenCookie != null) {
                try {
                    Claims claims = JwtTokenUtil.getClaims(tokenCookie.getValue(), appProperties.getJwtSecretKey());
                    request.setAttribute(AppConstants.CLAIMS, claims);
                    // 延迟有效时间
                    String token = JwtTokenUtil.generateToken(claims, appProperties.getJwtExpiration(), appProperties.getJwtSecretKey());
                    // 更新Cookie
                    CookieUtil.createCookie(AppConstants.TOKEN, token, "127.0.0.1", 3600, true, response);
                    CookieUtil.createCookie(AppConstants.USER_NAME, userNameCookie.getValue(), "127.0.0.1", 3600, response);
                } catch (SignatureException e) {
                    throw new ApplicationException("非法请求，无效的Token！");
                } catch (ExpiredJwtException e) {
                    throw new ApplicationException("登录已过期，请重新登录！");
                } catch (JwtException e) {
                    throw new ApplicationException(e.getMessage(), e);
                }
                filterChain.doFilter(request, response);
            } else {
                throw new ApplicationException("受限内容，请登录后再访问！");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
