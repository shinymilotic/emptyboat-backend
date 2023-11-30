package overcloud.blog.infrastructure.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() {
        StringBuilder connectionString = new StringBuilder();
        connectionString.append("jdbc:postgresql://192.168.1.7:5432/realworld");

        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(connectionString.toString())
            .username("postgres")
            .password("123123").build();
    }
}
