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
        connectionString.append("jdbc:postgresql://localhost:5432/realworld?currentSchema=public");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(connectionString.toString());
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("123123");
        return dataSourceBuilder.build();
    }
}
