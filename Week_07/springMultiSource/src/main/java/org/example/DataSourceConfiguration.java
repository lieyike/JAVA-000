package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("secondDataSourceProperties")
    @ConfigurationProperties("spring.second-datasource")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource primaryDataSource(@Autowired DataSourceProperties props) {
        return props.initializeDataSourceBuilder().build();
    }

    @Bean("secondDatasource")
    public DataSource secondDataSource(@Autowired @Qualifier("secondDataSourceProperties") DataSourceProperties props) {
        return props.initializeDataSourceBuilder().build();
    }


    @Bean
    @Primary
    public JdbcTemplate primaryJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondJdbcTemplate")
    public JdbcTemplate secondJdbcTemplate(@Autowired @Qualifier("secondDatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
