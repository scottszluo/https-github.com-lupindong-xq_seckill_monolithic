package net.lovexq.background.estate.controller;

import net.lovexq.background.core.controller.BasicController;
import net.lovexq.background.estate.dto.EstateItemDTO;
import net.lovexq.background.estate.service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 房源控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@Controller
public class EstateController extends BasicController {

    @Autowired
    private EstateService estateService;

    @GetMapping("/estate/list")
    public String listWithGetUI(HttpServletRequest request, Model model) throws Exception {
        pageable = buildPageRequest(request);
        Page<EstateItemDTO> estateItemPage = estateService.listForSaleByPage(pageable, null);
        model.addAttribute("estateItemPage", estateItemPage);
        return "/estate/listUI";
    }

    @PostMapping("/estate/list")
    public String listWithPostUI(HttpServletRequest request, Model model) throws Exception {
        pageable = buildPageRequest(request);
        paramMap = buildParamMap(request);
        Page<EstateItemDTO> estateItemPage = estateService.listForSaleByPage(pageable, paramMap);
        model.addAttribute("estateItemPage", estateItemPage);
        model.addAllAttributes(paramMap);
        return "/estate/listUI";
    }

    @GetMapping("/estate/{houseCode}")
    public String detailUI(@PathVariable("houseCode") String houseCode, Model model) throws Exception {
        EstateItemDTO estateItem = estateService.getByHouseCode(houseCode);
        model.addAttribute("estateItem", estateItem);
        return "/estate/detailUI";
    }
}