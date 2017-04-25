package net.lovexq.seckill.core.controller;

/**
 * Created by lupindong on 16-12-15.
 */

import net.lovexq.seckill.common.model.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 基类控制类
 *
 * @author LuPindong
 * @date 2016-12-15
 */
public class BasicController {

    protected Pageable pageable;
    protected JsonResult result = new JsonResult();
    protected Map<String, String> paramMap = new HashMap<>();
    private int defaultPage = 1;
    private int defaultSize = 15;

    /**
     * 创建分页请求.
     */
    protected PageRequest buildPageRequest(HttpServletRequest request) {

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        String sortStr = request.getParameter("sort");

        int page = Integer.parseInt(pageStr != null ? pageStr : String.valueOf(defaultPage));
        int size = Integer.parseInt(sizeStr != null ? sizeStr : String.valueOf(defaultSize));

        if (page < 1) page = defaultPage;
        if (size < 1) size = defaultSize;

        if (StringUtils.isNotBlank(sortStr)) {
            Sort sort;
            String[] sortArray = sortStr.split(":");
            String orderField = sortArray[0];
            String orderType = sortArray[1];
            if (StringUtils.isNotBlank(orderType)) {
                if (Sort.Direction.DESC.equals(orderType.toUpperCase())) {
                    sort = new Sort(Sort.Direction.DESC, orderField);
                } else {
                    sort = new Sort(Sort.Direction.ASC, orderField);
                }
            } else {
                sort = new Sort(Sort.DEFAULT_DIRECTION, orderField);
            }
            return new PageRequest(page - 1, size, sort);
        } else {
            return new PageRequest(page - 1, size);
        }
    }

    protected Map<String, String> buildParamMap(HttpServletRequest request) {
        Map<String, String[]> maps = request.getParameterMap();
        if (!CollectionUtils.isEmpty(maps)) {
            maps.forEach((key, value) -> paramMap.put(key, value[0]));
        }
        return paramMap;
    }

    public JsonResult getResult() {
        return result;
    }

    public void setResult(JsonResult result) {
        this.result = result;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }
}