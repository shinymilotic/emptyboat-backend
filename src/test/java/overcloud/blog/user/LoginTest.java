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
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public LoginTest() {

    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("trungtin.mai1412@gmail.com")
                .password("123123123")
                .build();

        String loginRequestStr = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        LoginResponse targetObject = mapper.readValue(response, LoginResponse.class);

        UserEntity user = userRepository.findByEmail("trungtin.mai1412@gmail.com");

        assertEquals(targetObject.getUserResponse().getEmail(), user.getEmail());
        assertEquals(targetObject.getUserResponse().getUsername(), user.getUsername());
        assertEquals(targetObject.getUserResponse().getImage(), user.getImage());
    }

    @Test
    public void testUsernameNotNull() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .password("123123123")
                .build();

        String loginRequestStr = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);

        assertEquals(targetObject.getApiErrorDetails().get(0).getMessage(), "Email/password must be specified");
    }

    @Test
    public void testUsernameNotEmpty() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("")
                .password("123123123")
                .build();

        String loginRequestStr = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequestStr)).andReturn();

        String response = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        ApiError targetObject = mapper.readValue(response, ApiError.class);

        assertEquals(targetObject.getApiErrorDetails().get(0).getMessage(), "Email/password must be specified");
    }

}
