package net.lovexq.background.special.controller;

import io.jsonwebtoken.Claims;
import net.lovexq.background.core.controller.BasicController;
import net.lovexq.background.special.dto.SpecialStockDTO;
import net.lovexq.background.special.service.SpecialService;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.TimeUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 特价秒杀控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@RestController
public class SpecialController extends BasicController {

    @Autowired
    private SpecialService specialService;


    @GetMapping("/specials")
    public JsonResult listData() throws Exception {
        List<SpecialStockDTO> specialStockList = specialService.listForSecKill();
        result.setData(specialStockList);
        return result;
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