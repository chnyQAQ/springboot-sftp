package com.chny.sftp.pool.config;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.pool2.impl.EvictionPolicy;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sftp连接池配置
 */
@Component
@ConfigurationProperties(prefix = "sftp.pool")
public class SftpPoolProperties extends GenericObjectPoolConfig<ChannelSftp> {
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private boolean lifo;
    private boolean fairness;
    private long maxWaitMillis;
    private long minEvictableIdleTimeMillis;
    private long evictorShutdownTimeoutMillis;
    private long softMinEvictableIdleTimeMillis;
    private int numTestsPerEvictionRun;
    private EvictionPolicy<ChannelSftp> evictionPolicy; // 仅2.6.0版本commons-pool2需要设置
    private String evictionPolicyClassName;
    private boolean testOnCreate;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testWhileIdle;
    private long timeBetweenEvictionRunsMillis;
    private boolean blockWhenExhausted;
    private boolean jmxEnabled;
    private String jmxNamePrefix;
    private String jmxNameBase;

    public SftpPoolProperties() {
        super();
    }

    @Override
    public int getMaxTotal() {
        return super.getMaxTotal();
    }

    @Override
    public void setMaxTotal(int maxTotal) {
        super.setMaxTotal(maxTotal);
    }

    @Override
    public int getMaxIdle() {
        return super.getMaxIdle();
    }

    @Override
    public void setMaxIdle(int maxIdle) {
        super.setMaxIdle(maxIdle);
    }

    @Override
    public int getMinIdle() {
        return super.getMinIdle();
    }

    @Override
    public void setMinIdle(int minIdle) {
        super.setMinIdle(minIdle);
    }

    @Override
    public boolean getLifo() {
        return super.getLifo();
    }

    @Override
    public boolean getFairness() {
        return super.getFairness();
    }

    @Override
    public void setLifo(boolean lifo) {
        super.setLifo(lifo);
    }

    @Override
    public void setFairness(boolean fairness) {
        super.setFairness(fairness);
    }

    @Override
    public long getMaxWaitMillis() {
        return super.getMaxWaitMillis();
    }

    @Override
    public void setMaxWaitMillis(long maxWaitMillis) {
        super.setMaxWaitMillis(maxWaitMillis);
    }

    @Override
    public long getMinEvictableIdleTimeMillis() {
        return super.getMinEvictableIdleTimeMillis();
    }

    @Override
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        super.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
    }

    @Override
    public long getSoftMinEvictableIdleTimeMillis() {
        return super.getSoftMinEvictableIdleTimeMillis();
    }

    @Override
    public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
        super.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
    }

    @Override
    public int getNumTestsPerEvictionRun() {
        return super.getNumTestsPerEvictionRun();
    }

    @Override
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        super.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
    }

    @Override
    public long getEvictorShutdownTimeoutMillis() {
        return super.getEvictorShutdownTimeoutMillis();
    }

    @Override
    public void setEvictorShutdownTimeoutMillis(long evictorShutdownTimeoutMillis) {
        super.setEvictorShutdownTimeoutMillis(evictorShutdownTimeoutMillis);
    }

    @Override
    public boolean getTestOnCreate() {
        return super.getTestOnCreate();
    }

    @Override
    public void setTestOnCreate(boolean testOnCreate) {
        super.setTestOnCreate(testOnCreate);
    }

    @Override
    public boolean getTestOnBorrow() {
        return super.getTestOnBorrow();
    }

    @Override
    public void setTestOnBorrow(boolean testOnBorrow) {
        super.setTestOnBorrow(testOnBorrow);
    }

    @Override
    public boolean getTestOnReturn() {
        return super.getTestOnReturn();
    }

    @Override
    public void setTestOnReturn(boolean testOnReturn) {
        super.setTestOnReturn(testOnReturn);
    }

    @Override
    public boolean getTestWhileIdle() {
        return super.getTestWhileIdle();
    }

    @Override
    public void setTestWhileIdle(boolean testWhileIdle) {
        super.setTestWhileIdle(testWhileIdle);
    }

    @Override
    public long getTimeBetweenEvictionRunsMillis() {
        return super.getTimeBetweenEvictionRunsMillis();
    }

    @Override
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        super.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
    }

    @Override
    public EvictionPolicy<ChannelSftp> getEvictionPolicy() {
        return super.getEvictionPolicy();
    }

    @Override
    public String getEvictionPolicyClassName() {
        return super.getEvictionPolicyClassName();
    }

    @Override
    public void setEvictionPolicy(EvictionPolicy<ChannelSftp> evictionPolicy) {
        super.setEvictionPolicy(evictionPolicy);
    }

    @Override
    public void setEvictionPolicyClassName(String evictionPolicyClassName) {
        super.setEvictionPolicyClassName(evictionPolicyClassName);
    }

    @Override
    public boolean getBlockWhenExhausted() {
        return super.getBlockWhenExhausted();
    }

    @Override
    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        super.setBlockWhenExhausted(blockWhenExhausted);
    }

    @Override
    public boolean getJmxEnabled() {
        return super.getJmxEnabled();
    }

    @Override
    public void setJmxEnabled(boolean jmxEnabled) {
        super.setJmxEnabled(jmxEnabled);
    }

    @Override
    public String getJmxNameBase() {
        return super.getJmxNameBase();
    }

    @Override
    public void setJmxNameBase(String jmxNameBase) {
        super.setJmxNameBase(jmxNameBase);
    }

    @Override
    public String getJmxNamePrefix() {
        return super.getJmxNamePrefix();
    }

    @Override
    public void setJmxNamePrefix(String jmxNamePrefix) {
        super.setJmxNamePrefix(jmxNamePrefix);
    }

}
