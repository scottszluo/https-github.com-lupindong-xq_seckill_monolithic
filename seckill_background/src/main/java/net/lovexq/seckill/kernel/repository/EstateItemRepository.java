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

    EstateItemModel findByHouseCode(String houseCode);

    List<EstateItemModel> findByHouseCodeLike(String houseCode);

    @Modifying
    @Query("UPDATE EstateItemModel i SET saleState = '下架' WHERE i.houseCode=?1")
    void updateState(String houseCode);
}