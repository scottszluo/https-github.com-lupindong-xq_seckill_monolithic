package net.lovexq.background.crawler.repository;

import net.lovexq.background.core.repository.BasicRepository;
import net.lovexq.background.crawler.model.CrawlerRecordModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LuPindong on 2017-4-26.
 */
public interface CrawlerRecordRepository extends BasicRepository<CrawlerRecordModel, Long> {

    String DELETEREPEATRECORD_SQL = "DELETE FROM CrawlerRecordModel c WHERE c.batch = ?1 AND c.currentCode=?2";


    CrawlerRecordModel findByBatchAndHistoryCode(String batch, String houseCode);

    List<CrawlerRecordModel> findByBatchAndCurrentCode(String batch, String houseCode);

    @Modifying
    @Query(DELETEREPEATRECORD_SQL)
    void deleteRepeatRecord(String batch, String houseCode);

    List<CrawlerRecordModel> findByBatchAndStateIn(String batch, List<String> stateList);
}