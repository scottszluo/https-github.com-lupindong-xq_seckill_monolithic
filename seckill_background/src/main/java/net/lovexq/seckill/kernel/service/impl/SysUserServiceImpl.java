package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.CookieUtil;
import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.core.properties.AppProperties;
import net.lovexq.seckill.core.support.security.JwtClaims;
import net.lovexq.seckill.core.support.security.JwtTokenUtil;
import net.lovexq.seckill.kernel.model.SysUserModel;
import net.lovexq.seckill.kernel.repository.SysUserRepository;
import net.lovexq.seckill.kernel.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletResponse;

/**
 * @author LuPindong
 * @time 2017-05-01 09:43
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private AppProperties appProperties;

    @Override
    @Transactional
    public JsonResult executeSignUp(String account, String email, String cipher) throws Exception {
        account = new String(Base64Utils.decodeFromString(account));
        email = new String(Base64Utils.decodeFromString(email));
        cipher = new String(Base64Utils.decodeFromString(cipher));

        SysUserModel userModel = sysUserRepository.findByAccount(account);
        if (userModel != null && userModel.getId() != null) {
            return new JsonResult(400, "此用户账号已被注册！");
        }
        userModel = sysUserRepository.findByEmail(email);
        if (userModel != null && userModel.getId() != null) {
            return new JsonResult(400, "此邮箱地址已被注册！");
        }

        userModel = new SysUserModel(IdWorker.INSTANCE.nextId(), account, cipher, email);
        sysUserRepository.save(userModel);
        return new JsonResult();
    }

    @Override
    @Transactional
    public JsonResult executeSignIn(HttpServletResponse response, String account, String cipher) throws Exception {
        JsonResult result = new JsonResult();

        account = new String(Base64Utils.decodeFromString(account));
        cipher = new String(Base64Utils.decodeFromString(cipher));

        SysUserModel userModel = sysUserRepository.findByAccount(account);
        if (userModel == null || userModel.getId() == null) {
            userModel = sysUserRepository.findByEmail(account);
            if (userModel == null || userModel.getId() == null) {
                return new JsonResult(400, "账号/邮箱或密码有误，请重新输入！");
            }
        }
        if (!cipher.equals(userModel.getPassword())) {
            return new JsonResult(400, "账号/邮箱或密码有误，请重新输入！");
        }

        JwtClaims claims = new JwtClaims(userModel.getAccount());
        String token = JwtTokenUtil.generateToken(claims, appProperties.getJwtExpiration(), appProperties.getJwtSecretKey());
        result.setData(token);

        // 存入Cookie
        CookieUtil.createCookie(AppConstants.TOKEN, token, "127.0.0.1", 3600, true, response);
        CookieUtil.createCookie(AppConstants.USER_NAME, userModel.getName(), "127.0.0.1", 3600,  response);

        return result;
    }

    @Override
    public JsonResult getPublicSalt() {
        JsonResult jsonResult = new JsonResult();
        String publicSalt = appProperties.getPublicSalt();
        jsonResult.setData(Base64Utils.encodeToString(publicSalt.getBytes()));
        return jsonResult;
    }
}