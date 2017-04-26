package net.lovexq.seckill.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BasicRepository - Jpa实现的基类Repository
 *
 * @author LuPindong
 * @date 2016年6月2日 下午9:15:30
 */
@NoRepositoryBean
public interface BasicRepository<T, PK extends Serializable> extends JpaRepository<T, PK>, JpaSpecificationExecutor<T> {

    /**
     * 获取Map对象
     *
     * @param querySql 查询SQL
     * @param params   查询参数
     * @return
     */
    Map queryForMap(final String querySql, final Object... params);

    /**
     * 获取Map对象列表
     *
     * @param querySql 查询SQL
     * @param params   查询参数
     * @return
     */
    List<Map> queryForMapList(final String querySql, final Object... params);

    /**
     * 获取Obj对象
     *
     * @param querySql 查询SQL
     * @param params   查询参数
     * @return
     */
    Object queryForObject(final String querySql, final Object... params);

    /**
     * 获取Obj对象列表
     *
     * @param querySql 查询SQL
     * @param params   查询参数
     * @return
     */
    List<Object> queryForObjectList(final String querySql, final Object... params);


    /**
     * 根据sql执行变更操作
     *
     * @param executeSql
     * @param params
     */
    int executeUpdateBySql(final String executeSql, final Object... params);
}