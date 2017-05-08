package net.lovexq.background.estate.repository;

import net.lovexq.background.core.repository.BasicRepository;
import net.lovexq.background.estate.model.EstateImageModel;

/**
 * Created by LuPindong on 2017-4-20.
 */
public interface EstateImageRepository extends BasicRepository<EstateImageModel, String> {

    Long deleteByHouseCode(String houseCode);
}