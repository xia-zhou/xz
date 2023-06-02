package com.cydeer.common.did;

import com.cydeer.common.did.core.IdGenerator;
import com.cydeer.common.did.core.generator.LocalIdGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式ID生成服务 - 生成器工厂类.
 *
 * @author Fyduan on 2018/7/12
 */
public class IdGeneratorFactory {
    private static final String DEFAULT_ID_TAG = "default";

    private static final Map<String, IdGenerator> GENERATOR_MAP = new ConcurrentHashMap<>();

    public static IdGenerator getGenerator(String tag) {
        tag = StringUtils.defaultIfBlank(tag, DEFAULT_ID_TAG);
        IdGenerator generator = GENERATOR_MAP.get(tag);
        if (generator == null) {
            synchronized (IdGeneratorFactory.class) {
                if (null == (generator = GENERATOR_MAP.get(tag))) {
                    GENERATOR_MAP.put(tag, generator = new LocalIdGenerator());
                }
            }
        }
        return generator;
    }
}
