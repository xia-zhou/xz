package com.cydeer.common.did.core.generator;

import com.cydeer.common.did.core.IdBox;
import com.cydeer.common.did.core.IdGenerator;
import com.cydeer.common.did.core.IdGeneratorConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 本地ID生成实现.
 *
 * @author Fyduan on 2018/7/12
 * @author xjunyu on 2019/08/08
 */
@Slf4j
public class LocalIdGenerator implements IdGenerator {
    private static final long SEQUENCE_MASK = (1 << IdBox.SEQUENCE_BITS) - 1;
    private long sequence;

    private long lastTime;

    /**
     * 调用该方法，得到分布式唯一ID
     *
     * @return key type is @{@link IdBox}.
     */
    @Override
    public synchronized IdBox next() {
        long currentMillis = System.currentTimeMillis();
        // 每次取分布式唯一ID的时间不能少于上一次取时的时间

        // 如果同一毫秒范围内，那么自增，否则从0开始
        if (lastTime == currentMillis) {
            // 如果自增后的sequence值超过4096，那么等待直到下一个毫秒
            if (0L == (sequence = ++sequence & SEQUENCE_MASK)) {
                currentMillis = waitUntilNextTime(currentMillis);
            }
        } else {
            // 新ID的起始值从0~200间的随机数获取
            // 原因：跨毫秒时如果序列号总是归零，会导致分库分表时取模不均
            // see https://mp.weixin.qq.com/s?__biz=MjM5ODYxMDA5OQ==&mid=403837240&idx=1&sn=ae9f2bf0cc5b0f68f9a2213485313127
            // 其次，可保证在低并发且多台机器本地生成workerid相同的情况下，减少id碰撞的可能性
            sequence = RandomUtils.nextInt(0, 200);
        }
        // 更新lastTime的值，即最后一次获取分布式唯一ID的时间
        lastTime = currentMillis;

        if (log.isDebugEnabled()) {
            log.debug("{}-{}-{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(lastTime)),
                      IdGeneratorConfig.getWorkerId(), sequence);
        }

        // 从这里可知分布式唯一ID的组成部分；
        return new IdBox(currentMillis, IdGeneratorConfig.getWorkerId(), sequence);
    }

    /**
     * 获取下一毫秒的方法：死循环获取当前毫秒与lastTime比较，直到大于lastTime的值；
     *
     * @param lastTime 上一次生成ID 的时间
     * @return timestamp
     */
    private long waitUntilNextTime(final long lastTime) {
        long time = System.currentTimeMillis();
        while (time <= lastTime) {
            time = System.currentTimeMillis();
        }
        return time;
    }

}
