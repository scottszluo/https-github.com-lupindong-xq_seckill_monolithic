package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.common.exception.ApplicationException;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.IdWorker;
import net.lovexq.seckill.core.properties.AppProperties;
import net.lovexq.seckill.kernel.model.SysUserModel;
import net.lovexq.seckill.kernel.repository.SysUserRepository;
import net.lovexq.seckill.kernel.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

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
    public JsonResult executeSignup(String account, String email, String cipher) throws Exception {
        SysUserModel userModel = sysUserRepository.findByAccount(account);
        if (userModel != null && userModel.getId() != null) {
            return new JsonResult(400, "此用户账号已被注册！");
        }
        userModel = sysUserRepository.findByEmail(email);
        if (userModel != null && userModel.getId() != null) {
//            return new JsonResult(400, "此邮箱地址已被注册！");
            throw new ApplicationException("此邮箱地址已被注册！");
        }
        userModel = new SysUserModel(IdWorker.INSTANCE.nextId(), account, cipher, email);
        sysUserRepository.save(userModel);
        return new JsonResult();
    }

    @Override
    public JsonResult getPublicSalt() {
        JsonResult jsonResult = new JsonResult();
        String publicSalt = appProperties.getPublicSalt();
        jsonResult.setData(Base64Utils.encodeToString(publicSalt.getBytes()));
        return jsonResult;
    }
}