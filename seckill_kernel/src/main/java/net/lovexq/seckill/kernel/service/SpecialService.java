package net.lovexq.seckill.kernel.service;

import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import net.lovexq.seckill.kernel.model.SpecialStockModel;

import java.util.List;

/**
 * 特价秒杀业务层抽象类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
public interface SpecialService {

    List<SpecialStockModel> listForSecKill() throws Exception;

    SpecialStockDTO getByHouseId(String id) throws Exception;

}