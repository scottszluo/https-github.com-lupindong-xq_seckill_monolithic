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

    String FINDFORSECKILLLIST_SQL = "SELECT s.* FROM special_stock s JOIN system_config c ON c.config_value = s.batch WHERE s.sale_state = '在售' AND s.number > 0 AND c.config_key = 'special_batch'";

    String FINDFORSECKILLLIST_COUNT_SQL = "SELECT count(s.id) FROM special_stock s JOIN system_config c ON c.config_value = s.batch WHERE s.sale_state = '在售' AND s.number > 0 AND c.config_key = 'special_batch'";

    String REDUCENUMBER_SQL = "UPDATE SpecialStockModel s SET number = number - 1 WHERE s.id=?1 AND startTime < ?2 AND endTime >= ?2 AND number > 0";

    
    SpecialStockModel findByHouseCode(String houseCode);

    @Query(value = FINDFORSECKILLLIST_SQL, countQuery = FINDFORSECKILLLIST_COUNT_SQL, nativeQuery = true)
    List<SpecialStockModel> findForSecKillList();

    @Modifying
    @Query(REDUCENUMBER_SQL)
    int reduceNumber(Long id, LocalDateTime killTime);
}