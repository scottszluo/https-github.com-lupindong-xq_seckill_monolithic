package net.lovexq.seckill.kernel.controller;

import net.lovexq.seckill.core.controller.BasicController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 *
 * @author LuPindong
 * @time 2017-04-19 09:25
 */
@Controller
public class IndexController extends BasicController {

    @GetMapping("/")
    public String index() {
        return "forward:/estate/list";
    }
}