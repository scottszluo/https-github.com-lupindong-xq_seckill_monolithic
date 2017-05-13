package net.lovexq.background.system.service;

import net.lovexq.seckill.common.model.JsonResult;

import javax.servlet.http.HttpServletResponse;

/**
 * @author LuPindong
 * @time 2017-05-01 09:42
 */
public interface UserService {

    JsonResult executeSignUp(String account, String email, String cipher) throws Exception;

    JsonResult executeSignIn(HttpServletResponse response, String userAgent, String account, String cipher) throws Exception;

    JsonResult getPublicSalt();
}