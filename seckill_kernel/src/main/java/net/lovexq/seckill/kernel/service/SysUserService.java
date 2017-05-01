package net.lovexq.seckill.kernel.service;

import net.lovexq.seckill.common.model.JsonResult;

/**
 * @author LuPindong
 * @time 2017-05-01 09:42
 */
public interface SysUserService {

    JsonResult executeSignup(String account, String email, String cipher) throws Exception;

    JsonResult getPublicSalt();
}