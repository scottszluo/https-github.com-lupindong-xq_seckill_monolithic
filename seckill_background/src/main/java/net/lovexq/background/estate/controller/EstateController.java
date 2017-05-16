package net.lovexq.background.estate.controller;

import net.lovexq.background.core.controller.BasicController;
import net.lovexq.background.estate.dto.EstateItemDTO;
import net.lovexq.background.estate.service.EstateService;
import net.lovexq.seckill.common.model.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 房源控制层
 *
 * @author LuPindong
 * @time 2017-04-19 07:42
 */
@RestController
public class EstateController extends BasicController {

    @Autowired
    private EstateService estateService;

    @GetMapping("/estates")
    public JsonResult listWithGetUI(HttpServletRequest request) throws Exception {
        pageable = buildPageRequest(request);
        Map<String, Object> paramMap = buildParamMap(request);
        Page<EstateItemDTO> estateItemPage = estateService.listForSaleByPage(pageable, paramMap);
        result.setData(estateItemPage);
        return result;
    }

}