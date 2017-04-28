package net.lovexq.seckill.kernel.service.impl;

import net.lovexq.seckill.kernel.dto.SpecialStockDTO;
import net.lovexq.seckill.kernel.model.SpecialStockModel;
import net.lovexq.seckill.kernel.repository.SpecialStockRepository;
import net.lovexq.seckill.kernel.service.ImageService;
import net.lovexq.seckill.kernel.service.SpecialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 房源业务层实现类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
@Service
public class SpecialServiceImpl implements SpecialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialServiceImpl.class);
    @Autowired
    private SpecialStockRepository specialStockRepository;
    @Autowired
    private ImageService imageService;

    @Override
    @Transactional(readOnly = true)
    public List<SpecialStockModel> listForSecKill() throws Exception {
        return specialStockRepository.findBySaleStatus("在售");
    }

    @Override
    @Transactional(readOnly = true)
    public SpecialStockDTO getByHouseId(String id) throws Exception {
        SpecialStockDTO targetStock = new SpecialStockDTO();
        SpecialStockModel sourceStock = specialStockRepository.findByHouseId(id);
        if (sourceStock != null) {
            BeanUtils.copyProperties(sourceStock, targetStock);
            targetStock.setEstateImageList(imageService.listByHouseId(id));
        }
        return targetStock;
    }

}