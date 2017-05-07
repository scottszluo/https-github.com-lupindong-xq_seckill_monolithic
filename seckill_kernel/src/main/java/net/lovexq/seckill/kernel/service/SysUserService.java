package net.lovexq.seckill.kernel.service;

import net.lovexq.seckill.common.model.JsonResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author LuPindong
 * @time 2017-05-01 09:42
 */
public interface SysUserService {

    JsonResult executeSignUp(String account, String email, String cipher) throws Exception;

    JsonResult executeSignIn(HttpServletResponse response, String account, String cipher) throws Exception;

    JsonResult getPublicSalt();
}