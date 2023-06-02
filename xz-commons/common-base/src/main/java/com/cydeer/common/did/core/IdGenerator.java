package com.cydeer.common.did.core;

/**
 * 分布式ID生成器服务组件.
 *
 * @author Fyduan on 2018/7/12
 */
public interface IdGenerator {

    /**
     * 获取新ID.
     *
     * @return ID对象
     */
    IdBox next();

}
