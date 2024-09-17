package overcloud.blog.config.datasource;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

   @Bean
   public DataSource getDataSource() {
       StringBuilder connectionString = new StringBuilder();
       connectionString.append("jdbc:postgresql://localhost:5432/realworld");

       return DataSourceBuilder.create()
           .driverClassName("org.postgresql.Driver")
           .url(connectionString.toString())
           .username("postgres")
           .password("123123").build();
   }

    // @Bean
    // public HikariDataSource getDataSource() {
    //     HikariDataSource dataSource = new HikariDataSource();
    //     dataSource.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
    //     dataSource.addDataSourceProperty("user","postgres");
    //     dataSource.addDataSourceProperty("password","1231233");
    //     dataSource.addDataSourceProperty("serverName", "localhost");
    //     dataSource.addDataSourceProperty("portNumber","5432");
    //     dataSource.addDataSourceProperty("databaseName", "realworld");
//        dataSource.addDataSourceProperty("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");

//        dataSource.setConnectionTimeout(30000);
//        dataSource.setAutoCommit(true);
//        dataSource.setMaxLifetime(60000);
//        dataSource.setLeakDetectionThreshold(15000);
//        dataSource.setMinimumIdle(5);
//        dataSource.setIdleTimeout(0);
//        dataSource.setMaximumPoolSize(20);
//        dataSource.setRegisterMbeans(true);
    //     dataSource.setUsername("postgres");
    //     dataSource.setPassword("123123");

    //     return dataSource;
    // }



}
