package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.CrawlerRecordModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LuPindong on 2017-4-26.
 */
public interface CrawlerRecordRepository extends BasicRepository<CrawlerRecordModel, Long> {

    CrawlerRecordModel findByBatchAndHistoryCode(String batch, String houseCode);

    List<CrawlerRecordModel> findByBatchAndCurrentCode(String batch, String houseCode);

    @Modifying
    @Query("DELETE FROM CrawlerRecordModel c WHERE c.batch = ?1 AND c.currentCode=?2")
    void deleteRepeatRecord(String batch, String houseCode);

    List<CrawlerRecordModel> findByBatchAndStateIn(String batch, List<String> stateList);
}