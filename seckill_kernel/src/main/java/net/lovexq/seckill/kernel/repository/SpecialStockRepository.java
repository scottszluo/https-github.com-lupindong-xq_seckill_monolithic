package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.SpecialStockModel;

import java.util.List;

/**
 * Created by LuPindong on 2017-4-20.
 */

public interface SpecialStockRepository extends BasicRepository<SpecialStockModel, Long> {

    SpecialStockModel findByHouseId(String houseId);

    List<SpecialStockModel> findBySaleState(String saleState);
}