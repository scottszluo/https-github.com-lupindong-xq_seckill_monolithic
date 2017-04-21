package net.lovexq.seckill.core.controller;

/**
 * Created by lupindong on 16-12-15.
 */

import net.lovexq.seckill.common.model.JsonResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

/**
 * 基类控制类
 *
 * @author LuPindong
 * @date 2016-12-15
 */
public class BaseController {

    protected JsonResult result = new JsonResult();

    protected Pageable pageRequest;

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
        int pageNumber = Integer.parseInt(request.getParameter("pageNumber") == "" ? "0" : request.getParameter("pageNumber"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize") == "" ? "0" : request.getParameter("pageSize"));
        return new PageRequest(pageNumber - 1, pageSize);
    }
}