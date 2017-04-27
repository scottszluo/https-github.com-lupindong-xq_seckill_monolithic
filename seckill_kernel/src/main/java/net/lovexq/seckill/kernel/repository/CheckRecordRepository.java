package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.CheckRecordModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by LuPindong on 2017-4-26.
 */
public interface CheckRecordRepository extends BasicRepository<CheckRecordModel, Long> {

    CheckRecordModel findByBatchAndHistoryCode(String batch, String houseId);

    List<CheckRecordModel> findByBatchAndCurrentCode(String batch, String houseId);

    @Modifying
    @Query("delete from CheckRecordModel c where c.batch = ?1 and c.currentCode=?2")
    void deleteRepeatRecord(String batch, String houseId);

}