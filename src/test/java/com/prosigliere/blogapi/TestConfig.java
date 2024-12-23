package com.prosigliere.blogapi;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

@TestConfiguration
@Profile("test")  // Ensure this only applies to the test profile
public class TestConfig {

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource();  // Return a dummy DataSource
    }
}

