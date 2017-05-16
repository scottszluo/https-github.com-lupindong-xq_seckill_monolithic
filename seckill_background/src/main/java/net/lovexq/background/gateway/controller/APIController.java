package net.lovexq.background.gateway.controller;

import net.lovexq.background.core.controller.BasicController;
import net.lovexq.background.gateway.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * API网关
 *
 * @author LuPindong
 * @time 2017-05-08 20:06
 */
@RestController
@RequestMapping
public class APIController extends BasicController {

    @Autowired
    private APIService apiService;


    @GetMapping("/api/{apiKey}")
    public void route(HttpServletRequest request, HttpServletResponse response, @PathVariable("apiKey") String apiKey) {
        Map<String, Object> paramMap = buildParamMap(request);
        paramMap.put("pageable", buildPageRequest(request));
        String apiUrl = apiService.getAPIUrl(apiKey);
    }

}