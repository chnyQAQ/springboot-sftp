package com.chny.sftp.pool.config;

import org.apache.commons.pool2.impl.AbandonedConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

/**
 * sftp废弃对象跟踪配置
 */
@Component
@ConfigurationProperties(prefix = "sftp.abandoned")
public class SftpAbandonedConfig extends AbandonedConfig {

    private boolean removeAbandonedOnBorrow;
    private boolean removeAbandonedOnMaintenance;
    private int removeAbandonedTimeout;
    private boolean logAbandoned;
    private boolean requireFullStackTrace;
    private PrintWriter logWriter;
    private boolean useUsageTracking;

    public SftpAbandonedConfig() {
        super();
    }

    @Override
    public boolean getRemoveAbandonedOnBorrow() {
        return super.getRemoveAbandonedOnBorrow();
    }

    @Override
    public void setRemoveAbandonedOnBorrow(boolean removeAbandonedOnBorrow) {
        super.setRemoveAbandonedOnBorrow(removeAbandonedOnBorrow);
    }

    @Override
    public boolean getRemoveAbandonedOnMaintenance() {
        return super.getRemoveAbandonedOnMaintenance();
    }

    @Override
    public void setRemoveAbandonedOnMaintenance(boolean removeAbandonedOnMaintenance) {
        super.setRemoveAbandonedOnMaintenance(removeAbandonedOnMaintenance);
    }

    @Override
    public int getRemoveAbandonedTimeout() {
        return super.getRemoveAbandonedTimeout();
    }

    @Override
    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        super.setRemoveAbandonedTimeout(removeAbandonedTimeout);
    }

    @Override
    public boolean getLogAbandoned() {
        return super.getLogAbandoned();
    }

    @Override
    public void setLogAbandoned(boolean logAbandoned) {
        super.setLogAbandoned(logAbandoned);
    }

    @Override
    public boolean getRequireFullStackTrace() {
        return super.getRequireFullStackTrace();
    }

    @Override
    public void setRequireFullStackTrace(boolean requireFullStackTrace) {
        super.setRequireFullStackTrace(requireFullStackTrace);
    }

    @Override
    public PrintWriter getLogWriter() {
        return super.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter logWriter) {
        super.setLogWriter(logWriter);
    }

    @Override
    public boolean getUseUsageTracking() {
        return super.getUseUsageTracking();
    }

    @Override
    public void setUseUsageTracking(boolean useUsageTracking) {
        super.setUseUsageTracking(useUsageTracking);
    }

}
