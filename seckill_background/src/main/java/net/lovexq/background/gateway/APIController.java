package net.lovexq.background.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API网关
 *
 * @author LuPindong
 * @time 2017-05-08 20:06
 */
@RestController
//@RequestMapping("api")
public class APIController {


    @GetMapping("/x")
    public void index() {
        System.out.println("hello world");
    }
}