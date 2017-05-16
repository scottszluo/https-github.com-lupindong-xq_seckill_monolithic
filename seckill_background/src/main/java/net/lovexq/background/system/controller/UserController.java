package net.lovexq.background.system.controller;

import net.lovexq.background.core.controller.BasicController;
import net.lovexq.background.system.service.UserService;
import net.lovexq.seckill.common.model.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户控制层
 *
 * @author LuPindong
 * @time 2017-05-01 09:41
 */
@RestController
@RequestMapping("/user")
public class UserController extends BasicController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

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
            result = userService.executeSignUp(account, email, cipher);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new JsonResult(500, e.getMessage(), e);
        }
        return result;
    }

    @PostMapping("/signin")
    public JsonResult signin(HttpServletRequest request, HttpServletResponse response) {
        String userAgent = request.getHeader("User-Agent").toLowerCase();
        String account = request.getParameter("account");
        String cipher = request.getParameter("cipher");
        if (StringUtils.isBlank(account)) {
            return new JsonResult(400, "账号/邮箱不能为空！");
        }
        if (StringUtils.isBlank(cipher)) {
            return new JsonResult(400, "密码不能为空！");
        }
        try {
            result = userService.executeSignIn(response, userAgent, account, cipher);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new JsonResult(500, e.getMessage(), e);
        }
        return result;
    }

    @PostMapping("/signout")
    public JsonResult signout(HttpServletRequest request, HttpServletResponse response) {
        return userService.executeSignOut(request, response);
    }

    @GetMapping("/salt")
    public JsonResult salt() {
        return userService.getPublicSalt();
    }
}