package net.lovexq.background.estate.service.impl;

import net.lovexq.background.estate.dto.EstateItemDTO;
import net.lovexq.background.estate.model.EstateImageModel;
import net.lovexq.background.estate.model.EstateItemModel;
import net.lovexq.background.estate.repository.EstateImageRepository;
import net.lovexq.background.estate.repository.EstateItemRepository;
import net.lovexq.background.estate.repository.specification.EstateItemSpecification;
import net.lovexq.background.estate.service.EstateService;
import net.lovexq.seckill.common.utils.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 房源业务层实现类
 *
 * @author LuPindong
 * @time 2017-04-20 23:05
 */
@Service
public class EstateServiceImpl implements EstateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstateServiceImpl.class);
    @Autowired
    private EstateItemRepository estateItemRepository;
    @Autowired
    private EstateImageRepository estateImageRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<EstateItemDTO> listForSaleByPage(Pageable pageable, Map<String, String> paramMap) throws Exception {
        Page<EstateItemDTO> targetItemPage;
        List<EstateItemDTO> targetItemList = new ArrayList<>();

        Page<EstateItemModel> sourceItemPage = estateItemRepository.findAll(EstateItemSpecification.getForSaleSpec(paramMap), pageable);
        List<EstateItemModel> sourceItemList = sourceItemPage.getContent();

        if (CollectionUtils.isEmpty(sourceItemList)) {
            targetItemPage = new PageImpl<>(targetItemList);
            return targetItemPage;
        }

        for (EstateItemModel sourceItem : sourceItemList) {
            EstateItemDTO targetItem = new EstateItemDTO();
            BeanUtils.copyProperties(sourceItem, targetItem);
            // 如果是最近三天上架的
            if (targetItem.getUpdateTime().isAfter(TimeUtil.nowDateTime().minusDays(3))) {
                targetItem.setNew(true);
            }
            targetItemList.add(targetItem);
        }

        targetItemPage = new PageImpl<>(targetItemList, pageable, sourceItemPage.getTotalElements());
        return targetItemPage;
    }

    @Override
    @Transactional(readOnly = true)
    public EstateItemDTO getByHouseCode(String houseCode) throws Exception {
        EstateItemDTO targetItem = new EstateItemDTO();
        EstateItemModel sourceItem = estateItemRepository.findByHouseCode(houseCode);
        if (sourceItem != null) {
            BeanUtils.copyProperties(sourceItem, targetItem);
            targetItem.setEstateImageList(listByHouseCode(houseCode));
        }

        return targetItem;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstateImageModel> listByHouseCode(String id) {
        EstateImageModel estateImage = new EstateImageModel(null, id);
        Sort sort = new Sort(Sort.Direction.ASC, "pictureType");
        return estateImageRepository.findAll(Example.of(estateImage), sort);
    }
}