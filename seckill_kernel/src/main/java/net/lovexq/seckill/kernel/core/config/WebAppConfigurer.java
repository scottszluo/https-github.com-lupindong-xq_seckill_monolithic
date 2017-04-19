package net.lovexq.seckill.kernel.core.config;

import net.lovexq.seckill.kernel.core.interceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
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
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    /*@Bean
    public FilterRegistrationBean addAFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        SessionFilter sessionFilter = new SessionFilter();
        beanFactory.autowireBean(sessionFilter);
        registration.setFilter(sessionFilter);
        registration.addUrlPatterns("/background*//**");
        return registration;
    }*/
}
