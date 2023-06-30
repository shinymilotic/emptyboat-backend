package overcloud.blog.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.login.LoginRequest;
import overcloud.blog.application.user.login.LoginResponse;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.JwtUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    public LoginTest() {

    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trungtin.mai1412@gmail.com")
                .password("123123123")
                .build();

    }

    @Test
    public void testUsernameNotNull() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .password("123123123")
                .build();

    }

    @Test
    public void testUsernameNotEmpty() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("")
                .password("123123123")
                .build();

    }

}
