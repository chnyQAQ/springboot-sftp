package com.chny.sftp.pool.config.support;

import com.chny.sftp.pool.config.SftpPoolConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class SftpPool extends GenericObjectPool<Sftp> {


    /**
     * 创建一个{@link GenericObjectPool}对象池，跟踪使用后未返回给对象池的对象，防止对象泄漏。
     * @param factory         对象工厂
     * @param config          对象池配置
     * @param abandonedConfig 废弃对象跟踪配置
     */
    public SftpPool(SftpFactory factory, SftpPoolConfig config, SftpAbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }


}