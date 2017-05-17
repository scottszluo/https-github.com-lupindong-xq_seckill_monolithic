package net.lovexq.background.special.scheduler;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.core.repository.cache.StringRedisClient;
import net.lovexq.background.special.model.SpecialOrderModel;
import net.lovexq.background.special.service.SpecialService;
import net.lovexq.seckill.common.model.JsonResult;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Callable;

/**
 * 特价秒杀处理多线程
 *
 * @author LuPindong
 * @time 2017-04-22 02:02
 */
public class SecKillCallable implements Callable<JsonResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillCallable.class);

    private SecKillParam secKillParam;


    public SecKillCallable(SecKillParam secKillParam) {
        this.secKillParam = secKillParam;
    }

    @Override
    public JsonResult call() throws Exception {
        JsonResult result = new JsonResult();
        try {
            ByteRedisClient byteRedisClient = secKillParam.getByteRedisClient();
            StringRedisClient stringRedisClient = secKillParam.getStringRedisClient();
            SpecialService specialService = secKillParam.getSpecialService();
            Long stockId = secKillParam.getStockId();

            // 库存大于零的时候一直循序执行
            while (Integer.parseInt(stringRedisClient.get(AppConstants.CACHE_SPECIAL_STOCK_COUNT + stockId)) >= 0) {
                Set<SpecialOrderModel> specialOrderModelSet = byteRedisClient.zrange(AppConstants.CACHE_ZSET_SPECIAL_ORDER + stockId, SpecialOrderModel.class, 0, -1);
                int index = 0;
                for (SpecialOrderModel specialOrderModel : specialOrderModelSet) {
                    result = specialService.decreaseStock(specialOrderModel);
                    Long removeCount = byteRedisClient.zrem(AppConstants.CACHE_ZSET_SPECIAL_ORDER + stockId, index, index);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("成功移除[用户账户: " + specialOrderModel.getAccount() + "], [房源ID:" + specialOrderModel.getStockId() + "]的特价秒杀订单记录, removeCount: " + removeCount);
                    }
                    index++;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = new JsonResult(500, e.getMessage());
        }
        return result;
    }
}