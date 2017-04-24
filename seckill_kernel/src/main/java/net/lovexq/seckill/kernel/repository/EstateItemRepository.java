package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BaseJpaRepository;
import net.lovexq.seckill.kernel.model.EstateItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by LuPindong on 2017-4-20.
 */
public interface EstateItemRepository extends BaseJpaRepository<EstateItem, Long> {

    EstateItem findByHouseId(String houseId);

    Page<EstateItem> findBySaleStatus(String saleStatus, Pageable pageable);
}