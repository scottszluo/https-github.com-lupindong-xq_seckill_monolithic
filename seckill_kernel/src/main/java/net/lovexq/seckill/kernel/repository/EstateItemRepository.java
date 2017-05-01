package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.EstateItemModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LuPindong on 2017-4-20.
 */
public interface EstateItemRepository extends BasicRepository<EstateItemModel, Long> {

    EstateItemModel findByHouseId(String houseId);

    List<EstateItemModel> findByHouseIdLike(String houseId);

    @Modifying
    @Query("update EstateItemModel i set saleState = '下架' where i.houseId=?1")
    void updateState(String houseId);
}