package com.mxb.githubdata.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
public class FlywayConfiguration {
    private static final Logger LOG = LoggerFactory
            .getLogger(FlywayConfiguration.class);

    static final String JDBC_PREFIX = "jdbc:";

    @Bean(name = "flywayInitializer")
    public FlywayMigrationInitializer flywayMigrationInitializer(Flyway flyway,
                                                                 DataSourceProperties properties) {
        String url = properties.getUrl();
        if (!url.startsWith(JDBC_PREFIX)) {
            LOG.error("Database url:{} prefix is unknown", url);
            throw new RuntimeException("Failed to initialize the mysql database");
        }

        String dbUri = url.substring(JDBC_PREFIX.length());
        URI uri = URI.create(dbUri);
        String hostPort = uri.getHost() + ":" + uri.getPort();
        if (uri.getHost() == null) {
            hostPort = uri.getAuthority();
        }
        createDB(JDBC_PREFIX + uri.getScheme() + "://" + hostPort,
                properties.getUsername(),
                properties.getPassword(),
                uri.getPath().substring(1));

        return new FlywayMigrationInitializer(flyway, null);
    }

    private static void createDB(String url, String username, String password, String database) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(String.format("CREATE DATABASE IF NOT EXISTS `%s` CHARACTER SET utf8", database));
            }
        } catch (SQLException e) {
            LOG.error("Failed to create database", e);
            throw new RuntimeException("Failed to initialize database: " + e.getMessage());
        }
    }
}

