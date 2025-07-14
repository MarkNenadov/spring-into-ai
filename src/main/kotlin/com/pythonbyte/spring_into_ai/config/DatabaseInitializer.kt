package com.pythonbyte.spring_into_ai.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
@Profile("!test") // Skip this configuration in test profile
open class DatabaseInitializer {
    @Value("\${spring.datasource.url}")
    private val jdbcUrl: String? = null

    @Bean
    open fun initDatabase(jdbcTemplate: JdbcTemplate): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            // Only run for PostgreSQL
            if (isPostgres(jdbcUrl)) {
                if (!dbExists(jdbcTemplate)) {
                    createDatabase(jdbcTemplate)
                }
            }
        }
    }

    companion object {
        private const val DB_NAME = "spring_into_ai"

        fun isPostgres(jdbcUrl: String?) = jdbcUrl?.contains("postgresql") == true

        fun dbExists(jdbcTemplate: JdbcTemplate): Boolean {
            val dbExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT datname FROM pg_catalog.pg_database WHERE datname = ?)",
                Boolean::class.java,
                DB_NAME
            )

            return dbExists != null && dbExists
        }

        fun createDatabase(jdbcTemplate: JdbcTemplate) {
            jdbcTemplate.execute("CREATE DATABASE $DB_NAME")
            println("Created database: $DB_NAME")
        }
    }
}
