package net.lovexq.background.core.filter;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.core.repository.cache.StringRedisClient;
import net.lovexq.seckill.common.exception.ApplicationException;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 秒杀预处理过滤器
 *
 * @author LuPindong
 * @time 2017-05-07 10:32
 */
@Order(2)
@WebFilter(filterName = "preSecKillFilter", urlPatterns = "/special/*")
public class PreSecKillFilter implements Filter {

    @Autowired
    private StringRedisClient stringRedisClient;

    @Autowired
    private ByteRedisClient byteRedisClient;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        if (uri.contains("/special/") && uri.contains("/execution/")) {
            String id = (String) uri.subSequence("/special/".length(), uri.lastIndexOf("/execution/"));

            // 检查是否还要剩余库存，有的话就放行
            long stockCount = Long.parseLong(stringRedisClient.get(AppConstants.CACHE_SPECIAL_STOCK_COUNT + id));
            if (stockCount > 0) {
                filterChain.doFilter(request, response);
            } else {
                throw new ApplicationException("房源已售罄！");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
