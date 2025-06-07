package com.pythonbyte.spring_into_ai.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
open class DatabaseInitializer {
    @Value("\${spring.datasource.url}")
    private val jdbcUrl: String? = null

    @Bean
    open fun initDatabase(jdbcTemplate: JdbcTemplate): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            // Extract database name from JDBC URL
            val dbName = "spring_into_ai"
            val dbExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT datname FROM pg_catalog.pg_database WHERE datname = ?)",
                Boolean::class.java,
                dbName
            )
            if (dbExists == null || !dbExists) {
                jdbcTemplate.execute("CREATE DATABASE $dbName")
                println("Created database: $dbName")
            }
        }
    }
}
