package com.cydeer.common.did;

/**
 * 分布式ID生成服务 - 生成器工厂类.
 *
 * @author Fyduan on 2018/7/12
 */
public class IdFactory {
    /**
     * 生成一个ID.
     *
     * @param tag ID归属
     * @return id
     */
    public static Long getId(String tag) {
        return IdGeneratorFactory.getGenerator(tag).next().getId();
    }

}
