package net.lovexq.seckill.kernel.service;

import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.kernel.dto.EstateItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 房源业务层抽象类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
public interface EstateService {

    JsonResult invokeCrawler(String baseUrl, String region, Integer curPage, Integer totalPage) throws Exception;

    JsonResult invokeUpdateCrawler(String batch, String baseUrl, String region, Integer curPage, Integer totalPage) throws Exception;

    void saveCrawlerData(byte[] dataArray) throws Exception;

    void updateCrawlerData(byte[] dataArray) throws Exception;

    Page<EstateItemDto> findForSaleList(Pageable pageable, Map<String, String> paramMap) throws Exception;

    EstateItemDto findByHouseId(String id) throws Exception;

}