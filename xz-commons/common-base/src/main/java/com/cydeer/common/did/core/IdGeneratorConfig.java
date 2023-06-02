package com.cydeer.common.did.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生成workerid配置
 *
 * @author xujunyu * @since 2019/08/08 17:25
 */
@Slf4j
public class IdGeneratorConfig {

    /**
     * 数立目前机器命名规则
     * 环境-应用-机房机器编号
     */
    private static final Pattern PATTERN_SL_HOSTNAME = Pattern.compile("^.{2,4}-.*-[a-zA-Z][0-9]{1,3}$");

    /**
     * 分片workerid
     */
    @Getter
    private static long workerId;

    static {
        init();
    }

    /**
     * 如果运维指定了workerId，则使用该workerId，
     * 否则根据机器名生成workerId，
     * 如果获取机器名失败，则随机生成workerId
     */
    private static void init() {
        String workerIdStr = System.getProperty("sl.id.generator.worker.id");
        if (!Strings.isNullOrEmpty(workerIdStr)) {
            if (log.isDebugEnabled()) {
                log.debug("did use property: sl.id.generator.worker.id as workerId, value={}", workerIdStr);
            }
            workerId = Long.parseLong(workerIdStr);
            return;
        }
        workerIdStr = System.getenv("SL_ID_GENERATOR_WORKER_ID");
        if (!Strings.isNullOrEmpty(workerIdStr)) {
            if (log.isDebugEnabled()) {
                log.debug("did use env: SL_ID_GENERATOR_WORKER_ID as workerId, value={}", workerIdStr);
            }
            workerId = Long.parseLong(workerIdStr);
            return;
        }
        workerId = workerIdByLocalIpv4OrRandom();
        if (log.isDebugEnabled()) {
            log.debug("did use ipv4 to generate workerId, value={}", workerId);
        }
    }

    /**
     * 利用机器分配的ipv4进行workerId的生成
     *
     * @return
     */
    static long workerIdByLocalIpv4OrRandom() {
        byte[] address = null;
        try {
            address = Inet4Address.getLocalHost().getAddress();
            return workerIdByIp(address);
        } catch (UnknownHostException | IllegalArgumentException e) {
            int upperBound = new Double(1 << IdBox.LOGICAL_SHARD_ID_BITS).intValue();
            int randomWorkerId = RandomUtils.nextInt(0, upperBound);
            if (e instanceof UnknownHostException) {
                log.error("DID_WORKER_LOAD,分布式ID组件获取ipv4失败,随机指定workerIp={}", randomWorkerId);
            } else {
                log.error("DID_WORKER_LOAD,未知错误,{},随机指定workerIp={}", address, randomWorkerId);
            }
            return randomWorkerId;
        }
    }

    /**
     * 利用机器指定的hostname进行workerId生成
     *
     * @return
     */
    static long workerIdByHostnameOrRandom() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            return hashHostName(hostname);
        } catch (UnknownHostException | IllegalArgumentException e) {
            int upperBound = new Double(1 << IdBox.LOGICAL_SHARD_ID_BITS).intValue();
            int randomWorkerId = RandomUtils.nextInt(0, upperBound);
            if (e instanceof UnknownHostException) {
                log.error("DID_WORKER_LOAD,分布式ID组件获取hostName失败,随机指定workerIp={}", randomWorkerId);
            } else {
                log.error("DID_WORKER_LOAD,分布式ID组件解析hostName失败,{}不符合数立机器命名规范{环境}-{业务}-{机房机器编号},随机指定workerIp={}",
                          hostname, randomWorkerId);
            }
            return randomWorkerId;
        }
    }

    /**
     * 根据ip的后16位生成workerId
     * 输入相同的ip，会给出相同的workerIp。
     *
     * @param ipAddress
     * @return
     */
    static long workerIdByIp(byte[] ipAddress) {
        Preconditions.checkArgument(4 == ipAddress.length);
        return Math.abs((ipAddress[2] << 8) + ipAddress[3]) % (1 << IdBox.LOGICAL_SHARD_ID_BITS);
    }

    /**
     * 00 1111 1111
     * 0：机房标志
     * 1：机器标志
     *
     * @param hostName
     * @return
     */
    static long hashHostName(String hostName) {
        Matcher matcher = PATTERN_SL_HOSTNAME.matcher(hostName);
        if (matcher.matches()) {
            String[] split = hostName.split("-");
            String idcAndMachineId = split[2];
            String idc = idcAndMachineId.substring(0, 1);
            String machine = idcAndMachineId.substring(1);
            return (idc.hashCode() % 4 << (IdBox.LOGICAL_SHARD_ID_BITS - 2)) | (Long.valueOf(machine));
        }
        throw new IllegalArgumentException("hostName 不符合数立机器命名规范," + hostName);
    }


}
