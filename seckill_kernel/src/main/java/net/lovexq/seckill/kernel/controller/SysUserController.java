package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.CookieUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.core.controller.BasicController;
import net.lovexq.seckill.kernel.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户控制层
 *
 * @author LuPindong
 * @time 2017-05-01 09:41
 */
@Controller
@RequestMapping("/user")
public class SysUserController extends BasicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/register")
    public String registerUI() {
        return "/user/registerUI";
    }

    @ResponseBody
    @PostMapping("/signup")
    public JsonResult signup(HttpServletRequest request) {
        String account = request.getParameter("account");
        String email = request.getParameter("email");
        String cipher = request.getParameter("cipher");
        if (StringUtils.isBlank(account)) {
            return new JsonResult(400, "用户账号不能为空！");
        }
        if (StringUtils.isBlank(email)) {
            return new JsonResult(400, "邮箱地址不能为空！");
        }
        if (StringUtils.isBlank(cipher)) {
            return new JsonResult(400, "用户密码不能为空！");
        }
        try {
            result = sysUserService.executeSignUp(account, email, cipher);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new JsonResult(500, e.getMessage(), e);
        }
        return result;
    }

    @GetMapping("/login")
    public String loginUI() {
        return "/user/loginUI";
    }

    @ResponseBody
    @PostMapping("/signin")
    public JsonResult signin(HttpServletRequest request, HttpServletResponse response) {
        String account = request.getParameter("account");
        String cipher = request.getParameter("cipher");
        if (StringUtils.isBlank(account)) {
            return new JsonResult(400, "账号/邮箱不能为空！");
        }
        if (StringUtils.isBlank(cipher)) {
            return new JsonResult(400, "密码不能为空！");
        }
        try {
            result = sysUserService.executeSignIn(response, account, cipher);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new JsonResult(500, e.getMessage(), e);
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/signout")
    public JsonResult signout(HttpServletResponse response) {
        CookieUtil.removeCookie(AppConstants.TOKEN, "127.0.0.1", response);
        CookieUtil.removeCookie(AppConstants.USER_NAME, "127.0.0.1", response);
        return result;
    }

    @ResponseBody
    @GetMapping("/salt")
    public JsonResult salt() {
        return sysUserService.getPublicSalt();
    }
}