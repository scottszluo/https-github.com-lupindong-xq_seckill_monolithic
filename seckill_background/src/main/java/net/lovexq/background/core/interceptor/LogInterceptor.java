package net.lovexq.background.core.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志拦截器
 *
 * @author LuPindong
 * @time 2017-04-19 09:50
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);

    private static final String ENTER = "->>>开始调用: ";

    private static final String EXIT = "<<<-结束调用: ";

    private String userName;

    private long startTime;

    private long endTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String methodName = method.getName();
            sb.append(ENTER).append(methodName);

            //参数非空则打印参数
            Map<String, String[]> requestMap = request.getParameterMap();
            if (requestMap != null && !requestMap.isEmpty()) {
                sb.append("(");
                for (Map.Entry<String, String[]> entry : requestMap.entrySet()) {
                    sb.append("[");
                    sb.append(entry.getKey());
                    sb.append("=");
                    String[] valueArr = entry.getValue();
                    sb.append(valueArr[0].toString());
                    sb.append("]");
                }
                sb.append(")");
            }

            LOGGER.debug(sb.toString());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            endTime = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            String methodName = method.getName();
            sb.append(EXIT).append(methodName);
            LOGGER.debug(sb.toString());

            sb = new StringBuilder();

            sb.append("[方法: ").append(methodName).append("()");
            sb.append(", 耗时: ").append(endTime - startTime).append(" ms] ");
            LOGGER.debug(sb.toString());
        }
    }
}
