package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.EstateImageModel;

/**
 * Created by LuPindong on 2017-4-20.
 */
public interface EstateImageRepository extends BasicRepository<EstateImageModel, String> {

    Long deleteByHouseCode(String houseCode);
}