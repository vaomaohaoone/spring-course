package ru.otus.spring.course.config;

import com.github.mongobee.Mongobee;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.otus.spring.course.migration.MigrationConfig;

@Configuration
@EnableMongoRepositories(basePackages = "ru.otus.spring.course.repository")
public class MongoConfig {

    @Autowired
    private MongoClient mongo;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public Mongobee mongobee(Environment environment) {
        Mongobee runner = new Mongobee(mongo);
        runner.setDbName(database);
        runner.setChangeLogsScanPackage(MigrationConfig.class.getPackageName());
        runner.setSpringEnvironment(environment);
        return runner;
    }

}
