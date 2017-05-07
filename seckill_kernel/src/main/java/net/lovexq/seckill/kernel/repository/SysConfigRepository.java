package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.SysConfigModel;

/**
 * 系统配置
 *
 * @author LuPindong
 * @time 2017-04-29 23:53
 */
public interface SysConfigRepository extends BasicRepository<SysConfigModel, Integer> {

    SysConfigModel findByConfigKey(String key);
}
