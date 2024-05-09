package com.armoniacode.armoniaskills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionVerifier implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Successfully connected to MySQL Database!");

        } catch (SQLException e) {
            System.err.println("Error connecting to MySQL Database: " + e.getMessage());
        }
    }
}