package org.infrasoft.security.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Created by Rachit Bedi @10/31/2018.
 * Version 1.0
 */
@Configuration
public class DataSourceConfiguration {

    @Bean("oauthDataSource")
    @ConfigurationProperties(prefix = "oauth2.datasource")
    public DataSource oauthDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "oauth2.datasource")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}
