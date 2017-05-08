package net.lovexq.background.core.repository.impl;

import net.lovexq.background.core.repository.BasicRepository;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseJpaRepositoryImpl - Jpa实现的基类RepositoryImpl
 *
 * @author "LuPindong"
 * @date 2016年6月2日 下午9:15:49
 */
public class BasicRepositoryImpl<T, PK extends Serializable> extends SimpleJpaRepository<T, PK> implements BasicRepository<T, PK> {

    @PersistenceContext
    private final EntityManager entityManager;

    public BasicRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    // 获取Map对象
    @Override
    public Map queryForMap(String querySql, Object... params) {
        Query query = entityManager.createNativeQuery(querySql);
        SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
        nativeQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                nativeQuery.setParameter(i, params[i]);
            }
        }
        return (Map) nativeQuery.uniqueResult();
    }

    // 获取Map对象列表
    @Override
    public List<Map> queryForMapList(String querySql, Object... params) {
        Query query = entityManager.createNativeQuery(querySql);
        SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
        nativeQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                nativeQuery.setParameter(i, params[i]);
            }
        }
        return nativeQuery.list();
    }

    // 获取Obj对象
    @Override
    public Object queryForObject(String querySql, Object... params) {
        Query query = entityManager.createNativeQuery(querySql);
        SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                nativeQuery.setParameter(i, params[i]);
            }
        }
        return nativeQuery.uniqueResult();
    }

    // 获取Obj对象列表
    @Override
    public List<Object> queryForObjectList(String querySql, Object... params) {
        Query query = entityManager.createQuery(querySql);
        SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                nativeQuery.setParameter(i, params[i]);
            }
        }

        return nativeQuery.list();
    }

    @Override
    public int executeUpdateBySql(String executeSql, Object... params) {
        Query query = entityManager.createNativeQuery(executeSql);
        SQLQuery nativeQuery = query.unwrap(SQLQuery.class);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                nativeQuery.setParameter(i, params[i]);
            }
        }

        return nativeQuery.executeUpdate();
    }

}