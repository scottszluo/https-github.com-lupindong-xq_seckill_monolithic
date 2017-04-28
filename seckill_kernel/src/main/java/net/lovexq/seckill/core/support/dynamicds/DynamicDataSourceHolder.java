package net.lovexq.seckill.core.support.dynamicds;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * DynamicDataSourceHolder<br/>
 * 向spring datasource 提供数据源选取key，对外提供从slave选取数据源，或从master选取数据源方法。 默认从master选取数据源key
 *
 * @author LuPindong
 * @date 2015年12月19日 下午9:39:04
 */
@Component
public class DynamicDataSourceHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();    // 线程本地环境
    private static final Random RANDOM = new Random();
    public static DruidDataSource masterDS;
    public static Map<String, DruidDataSource> slaveDSMap = new HashMap<>();
    public static String masterDSKey;
    public static List<String> slaveDSKeys = new ArrayList<>();

    public static Object getDataSourceKey() {
        return dataSourceHolder.get();
    }

    private void setDataSource(String dataSourceKey) {
        dataSourceHolder.set(dataSourceKey);
    }

    /**
     * 标记选取slave数据源
     */
    public void markSlave() {
        if (dataSourceHolder.get() != null) {
            // 从现在的策略来看,不允许标记两次,直接抛异常,优于早发现问题
            throw new IllegalArgumentException("当前已有选取数据源,不允许覆盖,已选数据源key:" + dataSourceHolder.get());
        }
        String dataSourceKey = selectFromSlave();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取的Slave数据源Key为：" + dataSourceKey);
        }
        setDataSource(dataSourceKey);
    }

    /**
     * 标记选取master数据源
     */
    public void markMaster() {
        if (dataSourceHolder.get() != null) {
            // 从现在的策略来看,不允许标记两次,直接抛异常,优于早发现问题
            throw new IllegalArgumentException("当前已有选取数据源,不允许覆盖,已选数据源key:" + dataSourceHolder.get());
        }
        String dataSourceKey = selectFromMaster();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取的Master数据源Key为：" + dataSourceKey);
        }
        setDataSource(dataSourceKey);
    }

    /**
     * 删除己标记选取的数据源
     */
    public void markRemove() {
        dataSourceHolder.remove();
    }

    /**
     * 是否已经绑定datasource
     * 绑定：true
     * 没绑定：false
     *
     * @return
     */
    public boolean hasBindingDataSource() {
        boolean hasBinding = dataSourceHolder.get() != null;
        return hasBinding;
    }

    private String selectFromSlave() {
        if (slaveDSMap == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("提供可选取Slave数据源：{},将自动切换从主Master选取数据源", slaveDSMap);
            }
            return selectFromMaster();
        } else {
            return slaveDSKeys.get(RANDOM.nextInt(slaveDSKeys.size()));
        }
    }

    private String selectFromMaster() {
        return masterDSKey;
    }

}