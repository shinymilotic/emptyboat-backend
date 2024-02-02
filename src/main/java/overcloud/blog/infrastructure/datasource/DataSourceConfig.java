package overcloud.blog.infrastructure.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

//    @Bean
//    public DataSource getDataSource() {
//        StringBuilder connectionString = new StringBuilder();
//        connectionString.append("jdbc:postgresql://14.225.207.12:5432/realworld");
//
//        return DataSourceBuilder.create()
//            .driverClassName("org.postgresql.Driver")
//            .url(connectionString.toString())
//            .username("postgres")
//            .password("123123").build();
//    }

//    @Bean
//    public DataSource getDataSource() {
//        HikariConfig config = new HikariConfig();
//
//        config.setJdbcUrl("jdbc:postgresql://14.225.207.12:5432/realworld");
//        config.setUsername("postgres");
//        config.setPassword("123123");
//
//        config.setMaximumPoolSize(5);
//        config.setAutoCommit(false);
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//
//        DataSource datasource = new HikariDataSource(config);
//
//        return datasource;
//    }
}
