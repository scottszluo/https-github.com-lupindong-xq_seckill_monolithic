package net.lovexq.background.gateway.service.impl;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.gateway.service.APIService;
import net.lovexq.background.system.repository.SystemConfigRepository;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LuPindong
 * @time 2017-05-13 19:56
 */
@Service("apiService")
public class APIServiceImpl implements APIService {

    @Autowired
    private ByteRedisClient byteRedisClient;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Override
    public String getAPIUrl(String apiKey) {
        String cacheKey = AppConstants.CACHE_API_KEY + apiKey;
        String apiUrl = byteRedisClient.getByteObj(cacheKey, String.class);
        if (StringUtils.isBlank(apiUrl)) {
            apiUrl = systemConfigRepository.findByConfigKey(apiKey).getConfigValue();
            byteRedisClient.setByteObj(cacheKey, apiUrl);
        }
        return apiUrl;
    }

}

