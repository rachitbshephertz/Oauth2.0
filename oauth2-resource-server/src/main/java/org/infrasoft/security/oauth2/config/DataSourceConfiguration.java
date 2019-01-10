package org.infrasoft.security.oauth2.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by Chen Du @10/31/2018.
 * Version 1.0
 */
@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "oauth2.datasource")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("oauthDataSource")
    @ConfigurationProperties(prefix = "oauth2.datasource")
    public DataSource oauthDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    
}
