package net.lovexq.background.special.scheduler;

import net.lovexq.background.core.repository.cache.ByteRedisClient;
import net.lovexq.background.core.repository.cache.StringRedisClient;
import net.lovexq.background.special.model.SpecialStockModel;
import net.lovexq.background.special.service.SpecialService;
import net.lovexq.seckill.common.utils.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 秒杀调度程序<br/>用于启动下个整个点秒杀活动处理线程
 */
@Component
@EnableScheduling
public class SecKillScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillScheduler.class);

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    @Autowired
    private ByteRedisClient byteRedisClient;
    @Autowired
    private StringRedisClient stringRedisClient;
    @Autowired
    private SpecialService specialService;

    //@Scheduled(cron = "0 20/40 * * * ?") // 每个整点的第20/40分钟执行一次检查
    @Scheduled(fixedDelay = 30000) //每30秒执行1次
    public void startSpecialThread() throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(">>>>>> " + threadPoolExecutor );
        }

        // 获取待处理的时间点
        LocalDateTime now = TimeUtil.nowDateTime();
        //int nextHours = now.toLocalTime().plusHours(1).getHour();
        int nextHours = now.toLocalTime().getHour();
        String startTime = now.toLocalDate().toString();
        if (nextHours < 10) {
            startTime = startTime + " 0" + nextHours + ":00:00";
        } else {
            startTime = startTime + " " + nextHours + ":00:00";
        }

        // 获取待处理的记录
        //List<SpecialStockModel> readyStockList = specialService.listReadyStock(startTime, false);
        List<SpecialStockModel> readyStockList = specialService.listReadyStock(startTime, true);
        if (CollectionUtils.isNotEmpty(readyStockList)) {
            // 检查是否应生成该特价秒杀处理线程
            Map<Thread, StackTraceElement[]> threadMaps = Thread.getAllStackTraces();
            for (SpecialStockModel readyStock : readyStockList) {
                boolean isExist = false;
                String targetTreadName = "SpecialThread-" + readyStock.getId();
                for (Thread thread : threadMaps.keySet()) {
                    String sourceTreadName = thread.getName();
                    if (sourceTreadName.equals(targetTreadName)) isExist = true;
                }

                // 如果当前线程中还没有则生成
                if (!isExist) {
                    threadPoolExecutor.setThreadFactory(new SecKillThreadFactory(targetTreadName));
                    threadPoolExecutor.setKeepAliveTime(2, TimeUnit.HOURS);
                    threadPoolExecutor.submit(new SecKillCallable(new SecKillParam(byteRedisClient, stringRedisClient, specialService, readyStock.getId())));
                }
            }
        }
    }
}