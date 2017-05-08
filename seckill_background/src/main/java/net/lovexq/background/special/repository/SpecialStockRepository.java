package net.lovexq.background.special.repository;

import net.lovexq.background.core.repository.BasicRepository;
import net.lovexq.background.special.model.SpecialStockModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by LuPindong on 2017-4-20.
 */

public interface SpecialStockRepository extends BasicRepository<SpecialStockModel, Long> {

    SpecialStockModel findByHouseCode(String houseCode);

    List<SpecialStockModel> findByBatchAndSaleStateOrderByStartTime(String batch, String saleState);

    @Modifying
    @Query("UPDATE SpecialStockModel s SET number = number - 1 WHERE s.houseCode=?1 AND startTime < ?2 AND endTime >= ?2 AND number > 0")
    int reduceNumber(String houseCode, LocalDateTime killTime);
}