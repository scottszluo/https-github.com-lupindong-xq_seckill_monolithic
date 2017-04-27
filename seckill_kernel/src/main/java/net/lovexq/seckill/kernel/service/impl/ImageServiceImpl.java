package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.kernel.model.EstateImageModel;
import net.lovexq.seckill.kernel.repository.EstateImageRepository;
import net.lovexq.seckill.kernel.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LuPindong
 * @time 2017-04-27 08:48
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private EstateImageRepository estateImageRepository;

    @Override
    public List<EstateImageModel> listByHouseId(String id) {
        EstateImageModel estateImage = new EstateImageModel(null, id);
        Sort sort = new Sort(Sort.Direction.ASC, "pictureType");
        return estateImageRepository.findAll(Example.of(estateImage), sort);
    }
}