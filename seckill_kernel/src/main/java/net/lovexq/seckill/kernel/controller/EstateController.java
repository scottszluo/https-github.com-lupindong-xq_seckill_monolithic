package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.core.controller.BasicController;
import net.lovexq.seckill.kernel.dto.EstateItemDto;
import net.lovexq.seckill.kernel.service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 房产控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@Controller
public class EstateController extends BasicController {

    @Autowired
    private EstateService estateService;

    @ResponseBody
    @PostMapping("/estate/crawler")
    public JsonResult invokeCrawler(HttpServletRequest request) throws Exception {
        String baseUrl = request.getParameter("baseUrl");
        String region = request.getParameter("region");
        Integer curPage = Integer.valueOf(request.getParameter("curPage"));
        Integer totalPage = Integer.valueOf(request.getParameter("totalPage"));

        result = estateService.invokeCrawler(baseUrl, region, curPage, totalPage);
        return result;
    }

    @GetMapping("/estate")
    public String listUI(HttpServletRequest request, Model model) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "totalPrice");
        pageable = buildPageRequest(request, sort);
        Page<EstateItemDto> itemPage = estateService.findForSaleList(pageable);
        model.addAttribute("itemPage", itemPage);
        return "/estate/listUI";
    }

    @GetMapping("/estate/{id}")
    public String detailUI(@PathVariable("id") String id, Model model) {
        EstateItemDto itemDto = estateService.findByHouseId(id);
        model.addAttribute("itemDto", itemDto);
        return "/estate/detailUI";
    }
}