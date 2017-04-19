package net.lovexq.seckill.kernel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 房地产控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@Controller
@RequestMapping("/estate")
public class EstateController {

    @GetMapping("/list")
    public String list() {
        return "/estate/list";
    }
}