package net.lovexq.background.special.scheduler;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.core.repository.cache.StringRedisClient;
import net.lovexq.background.special.service.SpecialService;

/**
 * @author LuPindong
 * @time 2017-05-17 11:34
 */
public class SecKillParam {

    private ByteRedisClient byteRedisClient;

    private StringRedisClient stringRedisClient;

    private SpecialService specialService;

    private Long stockId;

    public SecKillParam(ByteRedisClient byteRedisClient, StringRedisClient stringRedisClient, SpecialService specialService, Long stockId) {
        this.byteRedisClient = byteRedisClient;
        this.stringRedisClient = stringRedisClient;
        this.specialService = specialService;
        this.stockId = stockId;
    }

    public ByteRedisClient getByteRedisClient() {
        return byteRedisClient;
    }

    public void setByteRedisClient(ByteRedisClient byteRedisClient) {
        this.byteRedisClient = byteRedisClient;
    }

    public StringRedisClient getStringRedisClient() {
        return stringRedisClient;
    }

    public void setStringRedisClient(StringRedisClient stringRedisClient) {
        this.stringRedisClient = stringRedisClient;
    }

    public SpecialService getSpecialService() {
        return specialService;
    }

    public void setSpecialService(SpecialService specialService) {
        this.specialService = specialService;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }
}
