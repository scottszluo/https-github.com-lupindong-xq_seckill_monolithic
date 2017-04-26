package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.EstateImage;

/**
 * Created by LuPindong on 2017-4-20.
 */
public interface EstateImageRepository extends BasicRepository<EstateImage, String> {
    
    Long deleteByHouseCode(String houseCode);
}