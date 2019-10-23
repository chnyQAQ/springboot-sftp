package com.chny.sftp.pool.config;

import com.jcraft.jsch.ChannelSftp;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
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
    private Pool pool = new Pool();

    public static class Pool extends GenericObjectPoolConfig<ChannelSftp> {

        /*
         * 对象最大数量
         * 默认值是8
         */
        private int maxTotal = DEFAULT_MAX_TOTAL;

        /*
         * 最大空闲对象数量
         * 默认值是8
         */
        private int maxIdle = DEFAULT_MAX_IDLE;

        /*
         * 最小空闲对象数量
         * 默认值是0
         */
        private int minIdle = DEFAULT_MIN_IDLE;

        /*
         * 检测空闲对象线程每次检测的空闲对象的数量。如果这个值小于0，则每次检测的空闲对象数量等于当前空闲对象数量除以这个值的绝对值，并对结果向上取整。
         * 默认值是3
         */
        private int numTestsPerEvictionRun = DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

        /*
         * 在检测空闲对象线程检测到对象不需要移除时，是否检测对象的有效性。
         * true是，默认值是false。建议配置为true，不影响性能，并且保证安全性。
         * 默认值是true
         */
        private boolean testWhileIdle = DEFAULT_TEST_WHILE_IDLE;

        /*
         * 空闲对象检测线程的执行周期，即多长时间执行一次空闲对象检测。
         * 单位是毫秒数。如果小于等于0，则不执行检测线程。
         * 默认值是1000L * 60L * 10L，即10分钟
         */
        private long timeBetweenEvictionRunsMillis = DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

        public Pool() {
            super();
        }

        @Override
        public int getMaxTotal() {
            return maxTotal;
        }

        @Override
        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        @Override
        public int getMaxIdle() {
            return maxIdle;
        }

        @Override
        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        @Override
        public int getMinIdle() {
            return minIdle;
        }

        @Override
        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        @Override
        public int getNumTestsPerEvictionRun() {
            return numTestsPerEvictionRun;
        }

        @Override
        public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
            this.numTestsPerEvictionRun = numTestsPerEvictionRun;
        }

        @Override
        public boolean getTestWhileIdle() {
            return testWhileIdle;
        }

        @Override
        public void setTestWhileIdle(boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        @Override
        public long getTimeBetweenEvictionRunsMillis() {
            return timeBetweenEvictionRunsMillis;
        }

        @Override
        public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        }

    }
}