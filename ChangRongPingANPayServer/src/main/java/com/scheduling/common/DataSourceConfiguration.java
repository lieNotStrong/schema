package com.scheduling.common;


import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;


@Configuration
public class DataSourceConfiguration {
    @Value("${spring.common-db.driverClassName}")
    private String driverClassName;
    @Value("${spring.common-db.url}")
    private String url;
    @Value("${spring.common-db.username}")
    private String username;
    @Value("${spring.common-db.password}")
    private String password;
    @Value("${spring.common-db.initialSize}")
    private Integer initialSize;
    @Value("${spring.common-db.minIdle}")
    private Integer minIdle;
    @Value("${spring.common-db.maxActive}")
    private Integer maxActive;
    @Value("${spring.common-db.maxWait}")
    private Long maxWait;
    @Value("${spring.common-db.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${spring.common-db.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${spring.common-db.validationQuery}")
    private String validationQuery;
    @Value("${spring.common-db.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${spring.common-db.testOnBorrow}")
    private Boolean testOnBorrow;
    @Value("${spring.common-db.testOnReturn}")
    private Boolean testOnReturn;
    @Value("${spring.common-db.filters}")
    private String filters;

    @org.springframework.context.annotation.Bean
    public DruidDataSource dataSource() throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(this.driverClassName);
        ds.setUrl(this.url);
        ds.setUsername(this.username);
        ds.setPassword(this.password);
        ds.setInitialSize(this.initialSize.intValue());
        ds.setMinIdle(this.minIdle.intValue());
        ds.setMaxActive(this.maxActive.intValue());
        ds.setMaxWait(this.maxWait.longValue());
        ds.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis.longValue());
        ds.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis.longValue());
        ds.setValidationQuery(this.validationQuery);
        ds.setTestWhileIdle(this.testWhileIdle.booleanValue());
        ds.setTestOnBorrow(this.testOnBorrow.booleanValue());
        ds.setTestOnReturn(this.testOnReturn.booleanValue());
        ds.setFilters(this.filters);

        return ds;
    }
}