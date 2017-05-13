package net.lovexq.background.estate.service.impl;

import net.lovexq.background.core.model.PageX;
import net.lovexq.background.core.repository.cache.RedisClient;
import net.lovexq.background.estate.dto.EstateItemDTO;
import net.lovexq.background.estate.model.EstateImageModel;
import net.lovexq.background.estate.model.EstateItemModel;
import net.lovexq.background.estate.repository.EstateImageRepository;
import net.lovexq.background.estate.repository.EstateItemRepository;
import net.lovexq.background.estate.repository.specification.EstateItemSpecification;
import net.lovexq.background.estate.service.EstateService;
import net.lovexq.seckill.common.utils.CacheKeyGenerator;
import net.lovexq.seckill.common.utils.CachedBeanCopier;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    private RedisClient redisClient;

    @Override
    @Transactional(readOnly = true)
    public Page<EstateItemDTO> listForSaleByPage(Pageable pageable, Map<String, String> paramMap) throws Exception {
        String cacheKey = CacheKeyGenerator.generate(EstateItemDTO.class, "listForSaleByPage", pageable, paramMap);

        Page<EstateItemDTO> targetItemPage = new PageX();
        //redisClient.del(cacheKey);
        // 读取缓存数据
        targetItemPage = redisClient.getObj(cacheKey, targetItemPage.getClass());
        if (targetItemPage != null && CollectionUtils.isNotEmpty(targetItemPage.getContent())) {
            return new PageX(targetItemPage.getContent(), pageable, targetItemPage.getTotalElements());
        } else {
            Page<EstateItemModel> sourceItemPage = estateItemRepository.findAll(EstateItemSpecification.getForSaleSpec(paramMap), pageable);
            List<EstateItemModel> sourceItemList = sourceItemPage.getContent();

            if (CollectionUtils.isNotEmpty(sourceItemList)) {
                List<EstateItemDTO> targetItemList = new ArrayList();
                for (EstateItemModel model : sourceItemList) {
                    EstateItemDTO dto = new EstateItemDTO();
                    CachedBeanCopier.copy(model, dto);

                    dto.setDetailHref("/special/" + dto.getHouseCode() + ".shtml");
                    dto.setTotalPriceStr(dto.getTotalPrice() + "万");
                    dto.setUnitPriceStr("单价" + dto.getUnitPrice() + "万");
                    dto.setDownPayments(dto.getUnitPriceStr() + ", 首付" + new BigDecimal(0.3).multiply(dto.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "万");
                    dto.setAreaStr(dto.getArea() + "平米");
                    dto.setFocusNumStr(dto.getFocusNum() + "人关注");
                    dto.setWatchNumStr(dto.getWatchNum() + "次带看");

                    targetItemList.add(dto);
                }
                targetItemPage = new PageX(targetItemList, pageable, sourceItemPage.getTotalElements());

                // 数据写入缓存
                redisClient.setObj(cacheKey, targetItemPage, 3600);
            }

            return targetItemPage;
        }
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