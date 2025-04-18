package com.increff.pos.spring;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
public class DBConfig {

    public static final String PACKAGE_POJO = "com.increff.pos.db.pojo";

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource bean = new BasicDataSource();
        bean.setDriverClassName(applicationProperties.getJdbcDriver());
        bean.setUrl(applicationProperties.getJdbcUrl());
        bean.setUsername(applicationProperties.getJdbcUsername());
        bean.setPassword(applicationProperties.getJdbcPassword());
        bean.setInitialSize(2);
        bean.setDefaultAutoCommit(applicationProperties.getSetDefaultAutoCommit());
        bean.setMinIdle(applicationProperties.getSetMinIdle());
        bean.setValidationQuery(applicationProperties.getSetValidationQuery());
        bean.setTestWhileIdle(applicationProperties.getSetTestWhileIdle());
        bean.setTimeBetweenEvictionRunsMillis(applicationProperties.getSetTimeBetweenEvictionRunsMillis());
        return bean;
    }

    @Bean(name = "entityManagerFactory")
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setPackagesToScan(PACKAGE_POJO);
        HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
        bean.setJpaVendorAdapter(jpaAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", applicationProperties.getHibernateDialect());
        jpaProperties.put("hibernate.show_sql", applicationProperties.getHibernateShowSql());
        jpaProperties.put("hibernate.hbm2ddl.auto", applicationProperties.getHibernateHbm2ddl());

        bean.setJpaProperties(jpaProperties);
        return bean;
    }

    @Bean(name = "transactionManager")
    @Autowired
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean emf) {
        JpaTransactionManager bean = new JpaTransactionManager();
        bean.setEntityManagerFactory(emf.getObject());
        return bean;
    }
}
