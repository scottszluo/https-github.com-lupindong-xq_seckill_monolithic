package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.core.controller.BasicController;
import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import net.lovexq.seckill.kernel.model.SpecialStockModel;
import net.lovexq.seckill.kernel.service.SpecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
    public String listUI(Model model) throws Exception {
        List<SpecialStockModel> specialStockList = specialService.listForSecKill();
        model.addAttribute("specialStockList", specialStockList);
        return "/special/listUI";
    }

    @GetMapping("/special/{id}")
    public String detailUI(@PathVariable("id") String id, Model model) throws Exception {
        SpecialStockDTO specialStock = specialService.getByHouseId(id);
        model.addAttribute("specialStock", specialStock);
        return "/special/detailUI";
    }
}