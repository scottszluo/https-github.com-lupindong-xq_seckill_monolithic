package net.lovexq.seckill.core.support.dynamicds;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源<br/>
 * 目前只针对一主，多从
 *
 * @author LuPindong
 * @time 2017-04-28 11:35
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDataSourceKey();
    }

}