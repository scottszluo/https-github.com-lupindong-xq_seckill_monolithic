package net.lovexq.background.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorController extends BasicErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @Autowired
    public ErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    /**
     * 覆盖默认的Json响应
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        LOGGER.error("捕获的Json异常信息为：{}", body);
        HttpStatus status = getStatus(request);

        //输出自定义的Json格式
        Map<String, Object> map = new HashMap();
        map.put("status", status.value());
        map.put("message", body.get("message"));
        map.put("data", body.get("error"));
        map.put("timestamp", body.get("timestamp"));

        return new ResponseEntity<>(map, status);
    }

    // 纯后台是不会有html响应的
    /**
     * 覆盖默认的HTML响应
     */
    /*@Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        LOGGER.error("捕获的Html异常信息为：{}", model);
        response.setStatus(getStatus(request).value());

        //根据状态码指定自定义的视图
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 404:
                return new ModelAndView("/error/404", model);
            case 500:
                return new ModelAndView("/error/500", model);
            default:
                return new ModelAndView("/error/error", model);
        }
    }*/
}