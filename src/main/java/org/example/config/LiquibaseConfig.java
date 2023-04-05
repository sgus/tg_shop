package org.example.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
@EnableTransactionManagement
public class LiquibaseConfig {
    @Value("${spring.liquibase.change-log}")
    private String changeLogPath;
    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(changeLogPath);
        liquibase.setContexts("development,test,production");
        liquibase.setChangeLogParameters(Collections.emptyMap()); // чтобы избежать ошибки, связанной с подстановкой параметров в YAML-файл
        return liquibase;
    }
}
