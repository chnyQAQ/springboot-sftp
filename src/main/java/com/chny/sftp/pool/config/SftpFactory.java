package com.chny.sftp.pool.config;

import com.jcraft.jsch.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Data
@Component
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SftpFactory extends BasePooledObjectFactory<ChannelSftp>{


    @Autowired
    private SftpProperties properties;

    /**
     * 创建一个{@link SftpTemplate}实例
     * 这个方法必须支持并发多线程调用
     * @return {@link SftpTemplate}实例
     */
    @Override
    public ChannelSftp create() {
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(properties.getUsername(), properties.getHost(), properties.getPort());
            sshSession.setPassword(properties.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            ChannelSftp channel = (ChannelSftp) sshSession.openChannel("sftp");
            channel.connect();
            System.out.println("创建成功！" + System.currentTimeMillis());
            return channel;
        } catch (JSchException e) {
            throw new SftpPoolException("连接sfpt失败", e);
        }
    }

    /**
     * 用{@link PooledObject}的实例包装对象
     * @param sftp 被包装的对象
     * @return 对象包装器
     */
    @Override
    public PooledObject<ChannelSftp> wrap(ChannelSftp sftp) {
        return new DefaultPooledObject<>(sftp);
    }

    /**
     * 销毁对象
     * @param p 对象包装器
     */
    @Override
    public void destroyObject(PooledObject<ChannelSftp> p) {
        ChannelSftp channelSftp = p.getObject();
        channelSftp.disconnect();
    }

    /**
     * 检查连接是否可用（根据配置的检查周期）
     * @param p 对象包装器
     * @return {@code true} 可用，{@code false} 不可用
     */
    @Override
    public boolean validateObject(PooledObject<ChannelSftp> p) {
        if (p != null) {
            ChannelSftp sftp = p.getObject();
            if (sftp != null) {
                try {
                    sftp.pwd();
                    return true;
                } catch (SftpException e) {
                    return false;
                }
            }
        }
        return false;
    }

}
