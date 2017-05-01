package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.SysUserModel;

/**
 * 系统用户
 *
 * @author LuPindong
 * @time 2017-04-29 23:53
 */
public interface SysUserRepository extends BasicRepository<SysUserModel, Long> {
    SysUserModel findByAccount(String account);

    SysUserModel findByEmail(String email);
}
