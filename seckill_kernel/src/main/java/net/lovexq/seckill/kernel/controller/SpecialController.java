package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.core.controller.BasicController;
import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import net.lovexq.seckill.kernel.service.SpecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 特价秒杀控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@Controller
public class SpecialController extends BasicController {

    @Autowired
    private SpecialService specialService;


    @GetMapping("/special/list")
    public String listWithGetUI(HttpServletRequest request, Model model) throws Exception {
        pageable = buildPageRequest(request);
        Page<SpecialStockDTO> specialStockPage = specialService.listForSecKillByPage(pageable);
        model.addAttribute("specialStockPage", specialStockPage);
        return "/special/listUI";
    }

    @PostMapping("/special/list")
    public String listWithPostUI(HttpServletRequest request, Model model) throws Exception {
        pageable = buildPageRequest(request);
        paramMap = buildParamMap(request);
        Page<SpecialStockDTO> specialStockPage = specialService.listForSecKillByPage(pageable);
        model.addAttribute("specialStockPage", specialStockPage);
        model.addAllAttributes(paramMap);
        return "/special/listUI";
    }

    @GetMapping("/special/{id}")
    public String detailUI(@PathVariable("id") String id, Model model) throws Exception {
        SpecialStockDTO specialStock = specialService.getByHouseId(id);
        model.addAttribute("specialStock", specialStock);
        return "/special/detailUI";
    }
}