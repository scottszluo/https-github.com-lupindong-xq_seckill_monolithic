package net.lovexq.background.system.repository;

import net.lovexq.background.core.repository.BasicRepository;
import net.lovexq.background.system.model.SystemUserModel;

/**
 * 系统用户
 *
 * @author LuPindong
 * @time 2017-04-29 23:53
 */
public interface SysUserRepository extends BasicRepository<SystemUserModel, Long> {
    SystemUserModel findByAccount(String account);

    SystemUserModel findByEmail(String email);
}
