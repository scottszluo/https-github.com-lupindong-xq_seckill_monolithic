package net.lovexq.seckill.kernel.repository;

import net.lovexq.seckill.core.repository.BasicRepository;
import net.lovexq.seckill.kernel.model.CheckRecord;

/**
 * Created by LuPindong on 2017-4-26.
 */
public interface CheckRecordRepository extends BasicRepository<CheckRecord, Long> {

    CheckRecord findByBatchAndHistoryCode(String batch, String houseId);
}