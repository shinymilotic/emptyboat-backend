package overcloud.blog.infrastructure.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() {
        StringBuilder connectionString = new StringBuilder();
        connectionString.append("jdbc:postgresql://34.143.156.213/postgres?currentSchema=public");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(connectionString.toString());
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("A\\Ss$yI$DEn47f~B");
        return dataSourceBuilder.build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
