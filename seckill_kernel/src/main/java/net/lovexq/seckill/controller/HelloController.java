package net.lovexq.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 演示控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/world")
    public void world() {
        System.out.println("world()");
    }
}