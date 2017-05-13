package net.lovexq.background.gateway.service.impl;

import net.lovexq.background.core.repository.cache.RedisClient;
import net.lovexq.background.gateway.service.APIService;
import net.lovexq.background.system.repository.SystemConfigRepository;
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
    private RedisClient redisClient;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Override
    public String getAPIUrl(String apiKey) {
        String apiUrl = redisClient.getStrValue(apiKey);
        if (StringUtils.isBlank(apiUrl)) {
            apiUrl = systemConfigRepository.findByConfigKey(apiKey).getConfigValue();
            redisClient.setStrValue(apiKey, apiUrl);
        }
        return apiUrl;
    }
}
