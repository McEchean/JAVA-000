package com.github.zibuyu28.homework112801.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;
import java.util.ArrayList;

@Configuration
@PropertySource("classpath:jdbc.properties")
public class DataSourceConfig {
    @Bean("master")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("slave1")
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource slave1DataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("slave2")
    @ConfigurationProperties(prefix = "spring.datasource.slave2")
    public DataSource slave2DataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public MultiDatasource multiDatasource(DataSource master, DataSource slave1, DataSource slave2) {
        ArrayList<DataSource> slaves = new ArrayList<>();
        slaves.add(slave1);
        slaves.add(slave2);
        return new MultiDatasource(master, slaves);
    }
}
