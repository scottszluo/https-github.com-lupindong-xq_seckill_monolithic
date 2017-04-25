package net.lovexq.seckill.core.controller;

/**
 * Created by lupindong on 16-12-15.
 */

import net.lovexq.seckill.common.model.JsonResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;

/**
 * 基类控制类
 *
 * @author LuPindong
 * @date 2016-12-15
 */
public class BasicController {

    protected JsonResult result = new JsonResult();

    protected Pageable pageable;

    public JsonResult getResult() {
        return result;
    }

    public void setResult(JsonResult result) {
        this.result = result;
    }

    /**
     * 创建分页请求.
     */
    protected PageRequest buildPageRequest(HttpServletRequest request) {
        int page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int size = Integer.parseInt(request.getParameter("size") != null ? request.getParameter("size") : "15");
        return new PageRequest(page - 1, size);
    }

    /**
     * 创建分页请求.
     */
    protected PageRequest buildPageRequest(HttpServletRequest request, Sort sort) {
        int page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int size = Integer.parseInt(request.getParameter("size") != null ? request.getParameter("size") : "15");
        return new PageRequest(page - 1, size, sort);
    }
}