package net.lovexq.background.core.controller;

/**
 * Created by lupindong on 16-12-15.
 */

import net.lovexq.seckill.common.model.JsonResult;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    private int defaultPage = 1; // 默认第一页
    private int maxPage = 100; // 最多显示前100页
    private int defaultSize = 15;

    /**
     * 创建分页请求.
     */
    protected PageRequest buildPageRequest(HttpServletRequest request) {
        int page = defaultPage;
        int size = defaultSize;

        String pageStr = request.getParameter("page");
        if (StringUtils.isNotBlank(pageStr)) page = Integer.parseInt(pageStr);
        String sizeStr = request.getParameter("size");
        if (StringUtils.isNotBlank(sizeStr)) size = Integer.parseInt(sizeStr);

        if (page < 1) page = defaultPage;
        if (page > 100) page = maxPage;
        if (size < 1) size = defaultSize;

        String sortStr = request.getParameter("sort");
        if (StringUtils.isNotBlank(sortStr)) {
            Sort sort;
            String[] sortArray = sortStr.split(":");
            String sortField = sortArray[0];
            String sortType = sortArray[1];
            if (StringUtils.isNotBlank(sortType)) {
                if ("DESC".equals(sortType.trim())) {
                    sort = new Sort(Sort.Direction.DESC, sortField);
                } else {
                    sort = new Sort(Sort.Direction.ASC, sortField);
                }
            } else {
                sort = new Sort(Sort.DEFAULT_DIRECTION, sortField);
            }
            return new PageRequest(page - 1, size, sort);
        } else {
            return new PageRequest(page - 1, size);
        }
    }

    protected Map<String, Object> buildParamMap(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap();
        Map<String, String[]> maps = request.getParameterMap();
        if (MapUtils.isNotEmpty(maps)) {
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
}