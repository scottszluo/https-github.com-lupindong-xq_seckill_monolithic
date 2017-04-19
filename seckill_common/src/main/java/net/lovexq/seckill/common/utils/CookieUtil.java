package net.lovexq.seckill.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    /**
     * 创建cookie
     *
     * @param name     cookie名字
     * @param context
     * @param maxAge
     * @param response
     * @return
     */
    public static Cookie createCookie(String name, String context, int maxAge, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, context); // 新建Cookie
        cookie.setDomain("localhost"); // 设置域名
        cookie.setMaxAge(maxAge); // 设置有效期
        cookie.setPath("/"); //设置路径，这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
        response.addCookie(cookie); // 输出到客户端
        return cookie;
    }

    /**
     * 删除cookie
     *
     * @param name     cookie名字
     * @param context
     * @param response
     * @return
     */
    public static void removeCookie(String name, String context, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, context); // 新建Cookie
        cookie.setMaxAge(0); // 设置生命周期为0，不能为负数
        response.addCookie(cookie); // 必须执行这一句
    }

    /**
     * 根据名字获取cookie
     *
     * @param request
     * @param name    cookie名字
     * @return
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = (Cookie) cookieMap.get(name);
            return cookie;
        } else {
            return null;
        }
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

}