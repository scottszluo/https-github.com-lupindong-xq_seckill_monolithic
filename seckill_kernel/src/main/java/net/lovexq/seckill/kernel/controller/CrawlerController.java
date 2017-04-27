package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.core.controller.BasicController;
import net.lovexq.seckill.kernel.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 爬虫控制器
 *
 * @author LuPindong
 * @time 2017-04-27 07:57
 */
@RestController
public class CrawlerController extends BasicController {

    @Autowired
    private CrawlerService crawlerService;

    @PostMapping("/crawler")
    public JsonResult invokeInitialize(HttpServletRequest request) throws Exception {
        String baseUrl = request.getParameter("baseUrl");
        String region = request.getParameter("region");
        Integer curPage = Integer.valueOf(request.getParameter("curPage"));
        Integer totalPage = Integer.valueOf(request.getParameter("totalPage"));
        return result = crawlerService.invokeInitialize(baseUrl, region, curPage, totalPage);
    }

    @PostMapping("/crawler/check")
    public JsonResult invokeCheck(HttpServletRequest request) throws Exception {
        String batch = request.getParameter("batch");
        String baseUrl = request.getParameter("baseUrl");
        Integer curPage = Integer.valueOf(request.getParameter("curPage"));
        Integer totalPage = Integer.valueOf(request.getParameter("totalPage"));
        return result = crawlerService.invokeCheck(batch, baseUrl, curPage, totalPage);
    }

}