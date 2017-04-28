package net.lovexq.seckill.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by LuPindong on 2017-4-19.
 */
public class IdWorkerTest {

    @Test
    public void nextId() throws Exception {
        long avg = 0;
        for (int k = 0; k < 10; k++) {
            List<Callable<Long>> partitions = new ArrayList<>();
            IdWorker idGen = IdWorker.INSTANCE;
            for (int i = 0; i < 1000000; i++) {
                partitions.add(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        Long id = idGen.nextId();
                        //System.out.println(id);
                        return id;
                    }
                });
            }
            ExecutorService executorPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            try {
                long s = System.currentTimeMillis();
                executorPool.invokeAll(partitions, 10000, TimeUnit.SECONDS);
                long s_avg = System.currentTimeMillis() - s;
                avg += s_avg;
                System.out.println("完成时间需要: " + s_avg / 1.0e3 + "秒");
                executorPool.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("平均完成时间需要: " + avg / 10 / 1.0e3 + "秒");
    }

    @Test
    public void getNextId() throws Exception {

        IdWorker idWorker = IdWorker.INSTANCE;
        IdWorker idWorker2 = IdWorker.INSTANCE;

        System.out.println(idWorker.equals(idWorker2));
        System.out.println("1:" + idWorker.nextId() + ",2:" + idWorker2.nextId());
    }

    @Test
    public void testUUID() {
        String id = UUID.randomUUID().toString();
        System.out.println(id);
        System.out.println(StringUtils.substring(id, 25));
        //149 31 91 80 69 11
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void testNum() {
        System.out.println(1 % 8);
        System.out.println(2 % 8);
        System.out.println(3 % 8);
        System.out.println(4 % 8);
        System.out.println(5 % 8);
        System.out.println(6 % 8);
        System.out.println(7 % 8);
        System.out.println(8 % 8);
        System.out.println(9 % 8);
        System.out.println(10 % 8);
        System.out.println(11 % 8);
        System.out.println(12 % 8);
        System.out.println(13 % 8);
        System.out.println(14 % 8);
        System.out.println(15 % 8);
        //System.out.println((10 % 8) > 6);
    }
}