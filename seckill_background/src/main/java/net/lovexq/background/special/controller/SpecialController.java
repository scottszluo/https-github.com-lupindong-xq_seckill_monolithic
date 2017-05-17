package net.lovexq.background.special.controller;

import io.jsonwebtoken.Claims;
import net.lovexq.background.core.controller.BasicController;
import net.lovexq.background.special.dto.SpecialStockDTO;
import net.lovexq.background.special.service.SpecialService;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.CaptchaGenerator;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.time.Instant;
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

    @PostMapping("/specials")
    public JsonResult createData() throws Exception {
        return specialService.createData();
    }

    @GetMapping("/specials")
    public JsonResult listData() throws Exception {
        List<SpecialStockDTO> specialStockList = specialService.listForSecKill();
        result.setData(specialStockList);
        return result;
    }

    @GetMapping("/special/nowTime")
    public JsonResult getNowTime() {
        return new JsonResult(Instant.now().toEpochMilli());
    }

    @GetMapping("/special/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Claims claims = (Claims) request.getAttribute(AppConstants.CLAIMS);

        // 缓存验证码数值
        CaptchaGenerator instance = CaptchaGenerator.INSTANCE;
        BufferedImage bi = instance.genImage();
        specialService.saveCaptcha(claims, instance.getCaptcha());

        // 输出验证码图片
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/JPEG");
        ImageIO.write(bi, "JPEG", response.getOutputStream());
        response.getOutputStream().flush();
    }


    @GetMapping("/special/{id}/exposure")
    public JsonResult exposureSecKillUrl(HttpServletRequest request, @PathVariable("id") String id) throws Exception {
        Claims claims = (Claims) request.getAttribute(AppConstants.CLAIMS);
        return specialService.exposureSecKillUrl(Long.valueOf(id), claims);
    }

    @PostMapping("/special/{id}/execution/{key}")
    public JsonResult executeSecKill(HttpServletRequest request, @PathVariable("id") String id, @PathVariable("key") String key, @RequestParam("captcha") String captcha) throws Exception {
        Claims claims = (Claims) request.getAttribute(AppConstants.CLAIMS);
        result = specialService.executeSecKill(Long.valueOf(id), key, captcha, claims);
        return result;
    }

}