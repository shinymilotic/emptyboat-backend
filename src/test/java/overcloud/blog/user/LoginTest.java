package overcloud.blog.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import overcloud.blog.application.user.login.LoginRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    public LoginTest() {

    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trungtin.mai1412@gmail.com")
                .password("123123123")
                .build();

    }

    @Test
    void testUsernameNotNull() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .password("123123123")
                .build();

    }

    @Test
    void testUsernameNotEmpty() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("")
                .password("123123123")
                .build();

    }

}
