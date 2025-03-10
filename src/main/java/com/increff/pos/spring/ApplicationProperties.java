package com.increff.pos.spring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ApplicationProperties {
    @Value("${app.version}")
    private String appVersion;

    @Value("${app.name}")
    private String appName;

    @Value("${jdbc.driverClassName}")
    private String jdbcDriver;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddl;

    @Value("${validation.query:SELECT 1}")
    private String setValidationQuery;

    @Value("${default.auto.commit:false}")
    private Boolean setDefaultAutoCommit;

    @Value("${min.idle:2}")
    private int setMinIdle;

    @Value("${test.while.idle:true}")
    private Boolean setTestWhileIdle;

    @Value("${time.between.eviction.runs.millis:60000}")
    private long setTimeBetweenEvictionRunsMillis;

    @Value("${tsv.max.lines:5000}")
    private int maxTsvLines;
}