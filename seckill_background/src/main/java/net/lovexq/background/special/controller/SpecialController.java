package net.lovexq.background.special.controller;

import io.jsonwebtoken.Claims;
import net.lovexq.background.special.dto.SpecialStockDTO;
import net.lovexq.background.special.service.SpecialService;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.TimeUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.background.core.controller.BasicController;
import net.lovexq.background.special.model.SpecialStockModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/special/{houseCode}")
    public String detailUI(@PathVariable("houseCode") String houseCode, Model model) throws Exception {
        SpecialStockDTO specialStock = specialService.getByHouseCode(houseCode);
        model.addAttribute("specialStock", specialStock);
        model.addAttribute("nowTimeX", TimeUtil.format(TimeUtil.nowDateTime()));
        model.addAttribute("startTimeX", TimeUtil.format(specialStock.getStartTime()));
        model.addAttribute("endTimeX", TimeUtil.format(specialStock.getEndTime()));

        return "/special/detailUI";
    }

    @ResponseBody
    @GetMapping("/special/now")
    public JsonResult now() {
        return new JsonResult(TimeUtil.format(TimeUtil.nowDateTime()));
    }

    @ResponseBody
    @GetMapping("/special/{houseCode}/exposure")
    public JsonResult exposure(HttpServletRequest request, @PathVariable("houseCode") String houseCode) throws Exception {
        Claims claims = (Claims) request.getAttribute(AppConstants.CLAIMS);
        return specialService.getExposureSecKillUrl(houseCode, claims);
    }

    @ResponseBody
    @PostMapping("/special/{houseCode}/execution/{key}")
    public JsonResult execution(HttpServletRequest request, @PathVariable("houseCode") String houseCode, @PathVariable("key") String key) throws Exception {
        Claims claims = (Claims) request.getAttribute(AppConstants.CLAIMS);
        result = specialService.executionSecKill(houseCode, key, claims);
        return result;
    }

}