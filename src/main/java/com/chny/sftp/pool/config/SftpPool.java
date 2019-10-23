package com.chny.sftp.pool.config;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.stereotype.Component;

@Component
public class SftpPool {

    private GenericObjectPool<ChannelSftp> pool;
    /**
     * 创建一个{@link GenericObjectPool}对象池，跟踪使用后未返回给对象池的对象，防止对象泄漏。
     * @param factory         对象工厂
     */
    public SftpPool(SftpFactory factory) {
        this.pool = new GenericObjectPool<>(factory, factory.getProperties().getPool());
    }


    /**
     * 获取一个sftp连接对象
     * @return sftp连接对象
     */
    public ChannelSftp borrowObject() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new SftpPoolException("获取ftp连接失败", e);
        }
    }

    /**
     * 归还一个sftp连接对象
     * @param channelSftp sftp连接对象
     */
    public void returnObject(ChannelSftp channelSftp) {
        if (channelSftp!=null) {
            pool.returnObject(channelSftp);
        }
    }

}