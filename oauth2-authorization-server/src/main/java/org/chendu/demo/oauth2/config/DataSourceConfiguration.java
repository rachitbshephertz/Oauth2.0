package org.chendu.demo.oauth2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Chen Du @10/31/2018.
 * Version 1.0
 */
@Configuration
public class DataSourceConfiguration {

    @Bean("oauthDataSource")
    @ConfigurationProperties(prefix = "oauth2.datasource")
    public DataSource oauthDataSource() {
        return DataSourceBuilder.create().build();
    }
}
