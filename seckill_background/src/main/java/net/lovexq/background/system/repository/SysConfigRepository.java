package net.lovexq.background.system.repository;

import net.lovexq.background.core.repository.BasicRepository;
import net.lovexq.background.system.model.SystemConfigModel;

/**
 * 系统配置
 *
 * @author LuPindong
 * @time 2017-04-29 23:53
 */
public interface SysConfigRepository extends BasicRepository<SystemConfigModel, Integer> {

    SystemConfigModel findByConfigKey(String key);
}
