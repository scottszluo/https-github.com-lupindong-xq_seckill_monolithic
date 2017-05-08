package net.lovexq.background.core.config;

import net.lovexq.background.core.filter.AuthenticationFilter;
import net.lovexq.background.core.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WebApp配置
 *
 * @author LuPindong
 * @time 2017-04-19 09:47
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {


    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean addAuthenticationFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(authenticationFilter);
        registration.addUrlPatterns("/special/*");
        return registration;
    }
}
