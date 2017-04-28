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

    CrawlerRecordModel findByBatchAndHistoryCode(String batch, String houseId);

    List<CrawlerRecordModel> findByBatchAndCurrentCode(String batch, String houseId);

    @Modifying
    @Query("delete from CrawlerRecordModel c where c.batch = ?1 and c.currentCode=?2")
    void deleteRepeatRecord(String batch, String houseId);

    List<CrawlerRecordModel> findByBatchAndStatusIn(String batch, List<Integer> statusList);
}