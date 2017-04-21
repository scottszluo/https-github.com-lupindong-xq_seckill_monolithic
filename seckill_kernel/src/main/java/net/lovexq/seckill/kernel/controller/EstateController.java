package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.core.controller.BaseController;
import net.lovexq.seckill.kernel.service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 房产控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@Controller
@RequestMapping("/estate")
public class EstateController extends BaseController {

    @Autowired
    private EstateService estateService;

    @GetMapping("/list")
    public String list() {
        return "/estate/list";
    }


    @ResponseBody
    @PostMapping("/crawler")
    public JsonResult invokeCrawler(HttpServletRequest request, HttpServletResponse response) {
        try {
            String baseUrl = request.getParameter("baseUrl");
            String region = request.getParameter("region");
            Integer curPage = Integer.valueOf(request.getParameter("curPage"));
            Integer totalPage = Integer.valueOf(request.getParameter("totalPage"));
            estateService.invokeCrawler(baseUrl, region, curPage, totalPage);
        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}