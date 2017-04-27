package net.lovexq.seckill.kernel.service;

import net.lovexq.seckill.kernel.model.EstateImageModel;

import java.util.List;

/**
 * Created by 品冬 on 2017-4-27.
 */
public interface ImageService {
    List<EstateImageModel> listByHouseId(String id);
}
