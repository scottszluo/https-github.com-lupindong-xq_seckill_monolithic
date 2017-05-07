package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.utils.CacheKeyGenerator;
import net.lovexq.seckill.core.repository.cache.RedisClient;
import net.lovexq.seckill.kernel.model.SysConfigModel;
import net.lovexq.seckill.kernel.repository.SysConfigRepository;
import net.lovexq.seckill.kernel.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LuPindong
 * @time 2017-05-01 09:43
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Autowired
    private RedisClient redisClient;

    @Override
    public SysConfigModel getByConfigKey(String key) {
        String cacheKey = CacheKeyGenerator.generate(SysConfigModel.class, "getByConfigKey", key);

        SysConfigModel sysConfigModel = redisClient.getObj(cacheKey, SysConfigModel.class);
        if (sysConfigModel != null) {
            return sysConfigModel;
        } else {
            sysConfigModel = sysConfigRepository.findByConfigKey(key);
            redisClient.setObj(cacheKey, sysConfigModel);
            return sysConfigModel;
        }
    }
}