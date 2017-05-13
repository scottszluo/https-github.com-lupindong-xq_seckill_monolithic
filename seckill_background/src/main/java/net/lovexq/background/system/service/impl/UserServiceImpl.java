package net.lovexq.background.system.service.impl;

import net.lovexq.background.core.properties.AppProperties;
import net.lovexq.background.system.model.SystemUserModel;
import net.lovexq.background.system.repository.SysUserRepository;
import net.lovexq.background.system.service.UserService;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.CookieUtil;
import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.background.core.support.security.JwtClaims;
import net.lovexq.background.core.support.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author LuPindong
 * @time 2017-05-01 09:43
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private AppProperties appProperties;

    @Override
    @Transactional
    public JsonResult executeSignUp(String account, String email, String cipher) throws Exception {
        account = new String(Base64Utils.decodeFromString(account), AppConstants.CHARSET_UTF8);
        email = new String(Base64Utils.decodeFromString(email), AppConstants.CHARSET_UTF8);
        cipher = new String(Base64Utils.decodeFromString(cipher), AppConstants.CHARSET_UTF8);

        SystemUserModel userModel = sysUserRepository.findByAccount(account);
        if (userModel != null && userModel.getId() != null) {
            return new JsonResult(400, "此用户账号已被注册！");
        }
        userModel = sysUserRepository.findByEmail(email);
        if (userModel != null && userModel.getId() != null) {
            return new JsonResult(400, "此邮箱地址已被注册！");
        }

        userModel = new SystemUserModel(IdWorker.INSTANCE.nextId(), account, cipher, email);
        sysUserRepository.save(userModel);
        return new JsonResult();
    }

    @Override
    @Transactional
    public JsonResult executeSignIn(HttpServletResponse response, String account, String cipher) throws Exception {
        JsonResult result = new JsonResult();

        account = new String(Base64Utils.decodeFromString(account), AppConstants.CHARSET_UTF8);
        cipher = new String(Base64Utils.decodeFromString(cipher), AppConstants.CHARSET_UTF8);

        SystemUserModel userModel = sysUserRepository.findByAccount(account);
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
        CookieUtil.createCookie(AppConstants.USER_NAME, userModel.getName(), "127.0.0.1", 3600, response);

        return result;
    }

    @Override
    public JsonResult getPublicSalt() {
        JsonResult jsonResult = new JsonResult();
        String publicSalt = appProperties.getPublicSalt();
        try {
            jsonResult.setData(Base64Utils.encodeToString(publicSalt.getBytes(AppConstants.CHARSET_UTF8)));
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return jsonResult;
    }
}