package net.lovexq.background.core.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LuPindong on 2017-2-15.
 */
@Configuration
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);


    /*@Bean
    @Primary
    public DruidDataSource masterDataSource(@Value("${app.dataSource.master.name}") String dsName,
                                            @Value("${app.dataSource.master.driverClass}") String driver,
                                            @Value("${app.dataSource.master.url}") String url,
                                            @Value("${app.dataSource.master.username}") String username,
                                            @Value("${app.dataSource.master.password}") String password) throws SQLException {
        return getDataSource(dsName, driver, url, username, password);
    }

    private DruidDataSource getDataSource(String dsName, String driver, String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setName(dsName);
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(100);
        dataSource.setMaxWait(90000);
        dataSource.setValidationQuery("SELECT 'x'");
        // 超过时间限制是否回收
        dataSource.setRemoveAbandoned(true);
        // 超时时间,单位为秒。1800秒=30分钟
        dataSource.setRemoveAbandonedTimeout(1800);
        // 关闭abanded连接时输出错误日志
        dataSource.setLogAbandoned(true);

        // 配置过滤器
        WallFilter wallFilter = new WallFilter();
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(5000);
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setDataSourceLogEnabled(false);
        slf4jLogFilter.setConnectionLogEnabled(false);
        slf4jLogFilter.setConnectionLogErrorEnabled(true);
        slf4jLogFilter.setResultSetLogEnabled(false);
        slf4jLogFilter.setResultSetLogErrorEnabled(true);
        slf4jLogFilter.setStatementLogEnabled(false);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        slf4jLogFilter.setStatementSqlFormatOption(new SQLUtils.FormatOption(false, false));

        List filterList = new ArrayList();
        filterList.add(wallFilter);
        filterList.add(statFilter);
        filterList.add(slf4jLogFilter);
        dataSource.setProxyFilters(filterList);

        return dataSource;
    }*/

    @Bean
    public ServletRegistrationBean druidServlet() {
        LOGGER.info("Initializing DruidServlet Config");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("loginUsername", "admin");// 用户名
        initParameters.put("loginPassword", "admin@123!");// 密码
        initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
        initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
        //initParameters.put("deny", "192.168.20.38");// IP黑名单 (存在共同时，deny优先于allow)
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        LOGGER.info("Registration DruidFilter");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        return filterRegistrationBean;
    }

}