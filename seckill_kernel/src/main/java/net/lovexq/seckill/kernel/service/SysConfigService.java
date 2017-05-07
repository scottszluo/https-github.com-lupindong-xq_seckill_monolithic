package net.lovexq.seckill.kernel.service;

import net.lovexq.seckill.kernel.model.SysConfigModel;

/**
 * @author LuPindong
 * @time 2017-05-01 09:42
 */
public interface SysConfigService {

    SysConfigModel getByConfigKey(String key);
}