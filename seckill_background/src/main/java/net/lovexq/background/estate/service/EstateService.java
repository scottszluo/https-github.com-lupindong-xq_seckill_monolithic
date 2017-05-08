package net.lovexq.background.estate.service;

import net.lovexq.background.estate.dto.EstateItemDTO;
import net.lovexq.background.estate.model.EstateImageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 房源业务层抽象类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
public interface EstateService {

    Page<EstateItemDTO> listForSaleByPage(Pageable pageable, Map<String, String> paramMap) throws Exception;

    EstateItemDTO getByHouseCode(String houseCode) throws Exception;

    List<EstateImageModel> listByHouseCode(String id);
}