package com.springmasterclass.study;

import org.flywaydb.core.Flyway;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationRunner implements CommandLineRunner {
    private final Flyway userFlyway;
    private final Flyway productFlyway;

    public DatabaseMigrationRunner(Flyway userFlyway, Flyway productFlyway) {
        this.userFlyway = userFlyway;
        this.productFlyway = productFlyway;
    }

    @Override
    public void run(String... args) {
        userFlyway.migrate();
        productFlyway.migrate();
    }
}