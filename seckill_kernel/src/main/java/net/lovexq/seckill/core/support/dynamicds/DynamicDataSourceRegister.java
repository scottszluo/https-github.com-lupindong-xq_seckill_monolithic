package net.lovexq.seckill.core.support.dynamicds;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.wall.WallFilter;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态数据源注册
 *
 * @author LuPindong
 * @time 2017-04-28 07:00
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
    private PropertyValues dataSourcePropertyValues;
    private ConversionService conversionService = new DefaultConversionService();
    private Map<Object, Object> targetDataSources = new HashMap<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("动态数据源注册开始>>>");
        }

        // 添加主数据源
        targetDataSources.put("master", DynamicDataSourceHolder.masterDS);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("提供可选取Master数据源：{}", "master");
        }
        DynamicDataSourceHolder.masterDSKey = "master";

        // 添加从数据源
        targetDataSources.putAll(DynamicDataSourceHolder.slaveDSMap);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("提供可选取Slave数据源：{}", DynamicDataSourceHolder.slaveDSMap.keySet());
        }
        for (String key : DynamicDataSourceHolder.slaveDSMap.keySet()) {
            DynamicDataSourceHolder.slaveDSKeys.add(key);
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);

        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", DynamicDataSourceHolder.masterDS);
        mpv.addPropertyValue("targetDataSources", targetDataSources);

        registry.registerBeanDefinition("dataSource", beanDefinition);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<<<动态数据源注册结束");
        }
    }

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment env) {
        try {
            initMasterDataSource(env);
            initSlaveDataSources(env);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 初始化主数据源
     */
    private void initMasterDataSource(Environment env) throws SQLException {
        Map<String, Object> msMap = new RelaxedPropertyResolver(env, "app.dataSource.master").getSubProperties(".");
        DynamicDataSourceHolder.masterDS = buildDruidDataSource(msMap, "");
        otherPropertiesBinder(env, DynamicDataSourceHolder.masterDS);
    }

    /**
     * 初始化从数据源
     */
    private void initSlaveDataSources(Environment env) throws SQLException {
        Map<String, Object> ssMap = new RelaxedPropertyResolver(env, "app.dataSource.slaves").getSubProperties(".");
        int slaveSize = ssMap.size() / 5;
        for (int i = 1; i <= slaveSize; i++) {
            String slaveName = "slave" + i;
            DruidDataSource slaveDataSource = buildDruidDataSource(ssMap, slaveName + ".");
            otherPropertiesBinder(env, slaveDataSource);
            DynamicDataSourceHolder.slaveDSMap.put(slaveName, slaveDataSource);
        }
    }

    /**
     * 创建数据源，有前缀
     *
     * @param dataSourceMap
     * @param prefix
     * @return
     * @throws SQLException
     */
    private DruidDataSource buildDruidDataSource(Map<String, Object> dataSourceMap, String prefix) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setName(MapUtils.getString(dataSourceMap, prefix + "name"));
        dataSource.setDriverClassName(MapUtils.getString(dataSourceMap, prefix + "driverClass"));
        dataSource.setUrl(MapUtils.getString(dataSourceMap, prefix + "url"));
        dataSource.setUsername(MapUtils.getString(dataSourceMap, prefix + "username"));
        dataSource.setPassword(MapUtils.getString(dataSourceMap, prefix + "password"));

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
    }

    /**
     * 为DataSource绑定更多属性
     *
     * @param env
     * @param dataSource
     */
    private void otherPropertiesBinder(Environment env, DruidDataSource dataSource) {
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);
        dataBinder.setIgnoreInvalidFields(false);
        dataBinder.setIgnoreUnknownFields(true);

        if (dataSourcePropertyValues == null) {
            RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(env, "druid");
            Map<String, Object> properties = relaxedPropertyResolver.getSubProperties(".");
            Map<String, Object> newProperties = new HashMap<>(properties);

            // 排除已经设置的属性
            newProperties.remove("name");
            newProperties.remove("driverClass");
            newProperties.remove("url");
            newProperties.remove("username");
            newProperties.remove("password");
            dataSourcePropertyValues = new MutablePropertyValues(newProperties);
        }
        dataBinder.bind(dataSourcePropertyValues);
    }

}