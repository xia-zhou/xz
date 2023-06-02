package com.cydeer.common.did.core;

import lombok.Data;

/**
 * 分布式ID服务 - ID模型.
 * 基于Snowflake算法
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |01111111|11111111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |x-------|--------|--------|--------|--------|--------|--------|--------| 作为ID的保留位，统一各平台上BIT首位的定义分歧 - 1bit
 * |-xxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xx------|--------|--------| 单位为毫秒的时间戳 - 41bit，从{@link #EPOCH}往后数一共可使用69年
 * |--------|--------|--------|--------|--------|--xxxxxx|xxxx----|--------| 逻辑分片ID - 10bit，机器ID，最多支持1024台机器
 * |--------|--------|--------|--------|--------|--------|----xxxx|xxxxxxxx| 自增序列号 - 12bit，单机每毫秒最多2^12=4095个id
 *
 * @author Fyduan on 2018/7/12
 * @author xujunyu on 2019/8/8
 */
@Data
public class IdBox {
    /**
     * EPOCH就是起始时间，从2016-09-16 00:00:00开始的毫秒数
     */
    public static final long EPOCH = 1473955200000L;

    /**
     * 逻辑分片字节长度
     */
    public static final int LOGICAL_SHARD_ID_BITS = 10;
    /**
     * 增量序列号字节长度
     */
    public static final int SEQUENCE_BITS = 12;
    /**
     * 时间戳偏移量
     */
    public static final int TIMESTAMP_SHIFT = SEQUENCE_BITS + LOGICAL_SHARD_ID_BITS;
    /**
     * 逻辑分片偏移量
     */
    public static final int LOGICAL_SHARD_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 序列号的最大值bit - 4095 - 1111111111111
     */
    public static final int MAX_SEQUENCE = ~(-1 << SEQUENCE_BITS);
    /**
     * 逻辑分片的最大值bit - 1023 - 11111111111
     */
    public static final int MAX_LOGICAL_SHARD_ID = ~(-1 << LOGICAL_SHARD_ID_BITS);
    /**
     * 逻辑分片的最小值bit - 1 - 1
     */
    public static final int MIN_LOGICAL_SHARD_ID = 1;
    /**
     * 一次性批量获取ID数量最大值：4095+1
     */
    public static final int MAX_BATCH_SIZE = MAX_SEQUENCE + 1;

    /**
     * 实际ID
     */
    private final long id;
    /**
     * 毫秒
     */
    private final long timestamp;
    /**
     * 分区
     */
    private final long shardId;
    /**
     * 序列号
     */
    private final long sequence;

    /**
     * 解析ID
     *
     * @param id Id Long
     */
    private IdBox(long id) {
        this.id = id;
        timestamp = (id >>> 22) + EPOCH;
        // 2 ^ 10 -1 = 0x3FF --> 0000000001111111111
        // 将shareId位段数据抽出
        shardId = (id & (0x3FF << 12)) >> 12;
        // 2 ^ 12 - 1 = 0xFFF --> 000000111111111111
        sequence = id & 0xFFF;
    }

    public static IdBox parse(long id) {
        return new IdBox(id);
    }

    public static IdBox parse(String id) {
        return new IdBox(Long.parseLong(id));
    }

    public String getStrId() {
        return String.valueOf(id);
    }

    /**
     * 传入基本参数，生成一个Id对象
     *
     * @param timestamp unix时间戳
     * @param shardId   逻辑分片ID
     * @param sequence  序列号
     */
    public IdBox(long timestamp, long shardId, long sequence) {
        this.timestamp = timestamp;
        this.shardId = shardId;
        this.sequence = sequence;
        /*
         * 构建ID数据，对原始数据做移位运算，以获得64的ID
         * 格式：1222222222222222222222222222222222222222223333333333444444444444
         * 1.作为ID的保留位，统一各平台上BIT首位的定义分歧 - 1bit
         * 2.单位为毫秒的时间戳 - 41bit
         * 3.逻辑分片ID - 10bit
         * 4.自增序列号 - 12bit
         */
        // this.id = (timestamp << (10 + 12)) + (shardId << 12) + sequence;
        // 时间戳左移22位 - timestamp+0{22} | 逻辑分片ID左移12位 - logicalShardId + 0{12} | 递增序列号
        id = ((timestamp - EPOCH) << TIMESTAMP_SHIFT) | (shardId << LOGICAL_SHARD_ID_SHIFT) | sequence;
    }

}
