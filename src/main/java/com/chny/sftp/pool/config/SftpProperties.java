package com.chny.sftp.pool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {

    // 主机ip
    private String host;
    // 端口号
    private int port = 22;
    // 用户名
    private String username;
    // 密码
    private String password;

}