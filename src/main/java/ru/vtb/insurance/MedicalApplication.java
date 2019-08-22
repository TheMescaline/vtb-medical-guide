package ru.vtb.insurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@SpringBootApplication
public class MedicalApplication {

    @Bean
    @Profile("h2")
    @ConfigurationProperties(prefix = "spring.h2.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Profile("postgres")
    @ConfigurationProperties(prefix = "spring.postgres.datasource")
    public DataSource postgresDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Profile("heroku")
    @ConfigurationProperties(prefix = "spring.heroku.datasource")
    public DataSource herokuDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Profile("mysql")
    @ConfigurationProperties(prefix = "spring.mysql.datasource")
    public DataSource mysqlDatasource() {
        return DataSourceBuilder.create().build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MedicalApplication.class);
    }
}
