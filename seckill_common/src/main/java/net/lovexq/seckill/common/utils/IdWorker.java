package net.lovexq.seckill.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdWorker {

    private static final Logger LOG = LoggerFactory.getLogger(IdWorker.class);

    private long workerId;

    private long datacenterId;

    private long sequence = 0L;

    private long twepoch = 1492567686845L; //计算标记时间

    /**
     * 部署节点上限由2的（datacenterIdBits+workerIdBits）次方决定
     */
    private long workerIdBits = 5L; //节点ID长度，默认5

    private long datacenterIdBits = 5L; //数据中心ID长度，默认5

    private long maxWorkerId = -1L ^ (-1L << workerIdBits); //最大支持机器节点数0~31，一共32个

    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits); //最大支持数据中心节点数0~31，一共32个

    /**
     * 每毫秒生产ID数由2的（sequenceBits）次方决定
     */
    private long sequenceBits = 12L; //序列号长度，默认12

    private long workerIdShift = sequenceBits; //机器节点左移12位

    private long datacenterIdShift = sequenceBits + workerIdBits; //数据中心节点左移17位

    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; //时间毫秒数左移22位

    private long sequenceMask = -1L ^ (-1L << sequenceBits); //4095

    private long lastTimestamp = -1L;

    public IdWorker() {
        this(1L, 1L);
    }

    public IdWorker(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        LOG.info(
                String.format(
                        "worker starting. timestamp left shift %d, datacenter id bits %d, worker id bits %d, sequence bits %d, workerid %d",
                        timestampLeftShift,
                        datacenterIdBits,
                        workerIdBits,
                        sequenceBits,
                        workerId));
    }

    public static IdWorker get() {
        return IdGenHolder.instance;
    }

    public synchronized long nextId() {
        //获取当前毫秒数
        long timestamp = timeGen();
        //如果服务器时间有问题(时钟后退) 报错。
        if (timestamp < lastTimestamp) {
            LOG.error(String.format("clock is moving backwards.  Rejecting requests until %d.", lastTimestamp));
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        //如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            //sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
            sequence = (sequence + 1) & sequenceMask;
            //判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
            if (sequence == 0) {
                //自旋等待到下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        // 最后按照规则拼出ID。
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "IdWorker [workerId=" + workerId + ", datacenterId=" + datacenterId + ", sequence=" + sequence + "]";
    }

    private static class IdGenHolder {
        private static final IdWorker instance = new IdWorker();
    }

}
