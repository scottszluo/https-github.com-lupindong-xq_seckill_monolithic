package net.lovexq.seckill.kernel.service;

import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 特价秒杀业务层抽象类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
public interface SpecialService {

    Page<SpecialStockDTO> listForSecKillByPage(Pageable pageable) throws Exception;

    SpecialStockDTO getByHouseId(String id) throws Exception;

}