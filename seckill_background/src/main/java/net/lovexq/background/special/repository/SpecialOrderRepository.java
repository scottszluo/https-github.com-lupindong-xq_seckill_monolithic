package net.lovexq.background.special.repository;

import net.lovexq.background.core.repository.BasicRepository;
import net.lovexq.background.special.model.SpecialOrderModel;

/**
 * Created by LuPindong on 2017-4-20.
 */

public interface SpecialOrderRepository extends BasicRepository<SpecialOrderModel, Long> {

    SpecialOrderModel findByHouseCodeAndAccount(String houseCode, String account);
}