package net.lovexq.background.special.repository;

import net.lovexq.background.core.repository.BasicRepository;
import net.lovexq.background.special.dto.SpecialStockDTO;
import net.lovexq.background.special.model.SpecialStockModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by LuPindong on 2017-4-20.
 */

public interface SpecialStockRepository extends BasicRepository<SpecialStockModel, Long> {

    String FINDFORSECKILLLIST_SQL = "SELECT s.* FROM special_stock s JOIN system_config c ON c.config_value = s.batch WHERE s.end_time>= ?1 AND s.sale_state = '在售' AND s.number > 0 AND c.config_key = 'special_batch' ORDER BY s.start_time";

    String FINDFORSECKILLLIST_COUNT_SQL = "SELECT count(s.id) FROM special_stock s JOIN system_config c ON c.config_value = s.batch WHERE s.end_time>= ?1 AND s.sale_state = '在售' AND s.number > 0 AND c.config_key = 'special_batch'";

    String REDUCENUMBER_SQL = "UPDATE SpecialStockModel s SET number = number - 1 WHERE s.id=?1 AND startTime < ?2 AND endTime >= ?2 AND number > 0";

    String FINDREADYSTOCKLIST_1_SQL = "SELECT s.* FROM special_stock s JOIN system_config c ON c.config_value = s.batch WHERE s.start_time = ?1 AND c.config_key = 'special_batch'";

    String FINDREADYSTOCKLIST_2_SQL = "SELECT s.* FROM special_stock s JOIN system_config c ON c.config_value = s.batch WHERE s.start_time >= ?1 AND c.config_key = 'special_batch'";

    SpecialStockModel findByHouseCode(String houseCode);

    @Query(value = FINDFORSECKILLLIST_SQL, countQuery = FINDFORSECKILLLIST_COUNT_SQL, nativeQuery = true)
    List<SpecialStockModel> findForSecKillList(String nowTime);

    @Modifying
    @Query(REDUCENUMBER_SQL)
    int reduceNumber(Long id, LocalDateTime killTime);

    @Query(value = FINDREADYSTOCKLIST_1_SQL, nativeQuery = true)
    List<SpecialStockModel> findReadyStockList(String startTime);

    @Query(value = FINDREADYSTOCKLIST_2_SQL, nativeQuery = true)
    List<SpecialStockModel> findAllReadyStockList(String startTime);
}