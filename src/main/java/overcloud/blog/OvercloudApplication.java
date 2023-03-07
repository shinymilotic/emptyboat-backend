package overcloud.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class OvercloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(OvercloudApplication.class, args);
    }

}
