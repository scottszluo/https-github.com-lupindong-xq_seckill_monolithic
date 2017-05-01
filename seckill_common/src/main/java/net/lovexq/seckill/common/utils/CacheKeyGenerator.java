package net.lovexq.seckill.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;

/**
 * 缓存关键词生成器
 *
 * @author LuPindong
 * @time 2017-04-29 11:18
 */
public class CacheKeyGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheKeyGenerator.class);
    // custom cache key
    private static final int NO_PARAM_KEY = 0;
    private static final int NULL_PARAM_KEY = -1;

    public static <T> String generate(Class<T> targetClass, String methodName, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(targetClass.getSimpleName()).append(".").append(methodName).append(":");
        if (params.length == 0) {
            return key.append(NO_PARAM_KEY).toString();
        }
        for (Object param : params) {
            if (param == null) {
                LOGGER.warn("input null param for Spring cache, use default key={}", NULL_PARAM_KEY);
                key.append(NULL_PARAM_KEY);
            } else if (ClassUtils.isPrimitiveArray(param.getClass())) {
                int length = Array.getLength(param);
                for (int i = 0; i < length; i++) {
                    key.append(Array.get(param, i));
                    key.append(',');
                }
            } else if (ClassUtils.isPrimitiveOrWrapper(param.getClass()) || param instanceof String) {
                key.append(param);
            } else {
                key.append(param.hashCode());
            }
            key.append('-');
        }
        String finalKey = key.toString();
        LOGGER.debug("using cache key={}, hashCode={}", finalKey, finalKey.hashCode());
        return finalKey;
    }

}