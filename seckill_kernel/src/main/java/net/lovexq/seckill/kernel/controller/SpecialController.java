package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.TimeUtil;
import net.lovexq.seckill.core.controller.BasicController;
import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import net.lovexq.seckill.kernel.model.SpecialStockModel;
import net.lovexq.seckill.kernel.service.SpecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/special/{houseId}")
    public String detailUI(@PathVariable("houseId") String houseId, Model model) throws Exception {
        SpecialStockDTO specialStock = specialService.getByHouseId(houseId);
        model.addAttribute("specialStock", specialStock);
        model.addAttribute("nowTimeX", TimeUtil.format(LocalDateTime.now()));
        model.addAttribute("startTimeX", TimeUtil.format(specialStock.getStartTime()));
        model.addAttribute("endTimeX", TimeUtil.format(specialStock.getEndTime()));

        return "/special/detailUI";
    }

    @ResponseBody
    @GetMapping("/special/now")
    public JsonResult now() {
        return new JsonResult(TimeUtil.format(LocalDateTime.now()));
    }

    @ResponseBody
    @GetMapping("/special/{id}/exposure")
    public JsonResult exposure(@PathVariable("id") String id) throws Exception {
        return specialService.exposureSecKillUrl(id);
    }

    @ResponseBody
    @PostMapping("/special/{id}/execution/{key}")
    public JsonResult exposure(@PathVariable("id") String id, @PathVariable("key") String key) throws Exception {
        return specialService.exposureSecKillUrl(id);
    }

}