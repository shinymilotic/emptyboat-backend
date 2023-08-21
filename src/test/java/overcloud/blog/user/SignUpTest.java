package overcloud.blog.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.application.user.core.UserEntity;
import overcloud.blog.application.user.core.UserResponse;
import overcloud.blog.application.user.core.repository.UserRepository;
import overcloud.blog.application.user.register.RegisterRequest;
import overcloud.blog.application.user.register.RegisterService;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.exceptionhandling.ApiErrorDetail;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import overcloud.blog.infrastructure.security.service.SpringAuthenticationService;
import overcloud.blog.infrastructure.validation.ObjectsValidator;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class SignUpTest {

    @Mock
    private RegisterService registerService;

    @Mock
    private UserRepository userRepository;

    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    @BeforeEach
    void initUseCase() {
        SpringAuthenticationService authenticationService = Mockito.mock(SpringAuthenticationService.class);
        JwtUtils jwtUtils = new JwtUtils("signKey012345678901234567890123456789", Long.valueOf(3000000), Long.valueOf(9));
        ObjectsValidator<RegisterRequest> validator = new ObjectsValidator<>(messageSource());
        registerService = new RegisterService(userRepository, authenticationService, jwtUtils, validator, null);
    }

    public void assertApiError(ApiError apiError, String id, String message) {
        List<ApiErrorDetail> apiErrorDetail = apiError.getApiErrorDetails();

        for (ApiErrorDetail detail : apiErrorDetail) {
            if(detail.getId().equals(id)) {
                assertEquals(detail.getMessage(), message);
                return;
            }
        }

        assertFalse(false);
    }

    public UserResponse registerUser(RegisterRequest request) {
        when(userRepository.findByUsername(anyString())).thenAnswer(invocation -> {
/*
            String username = invocation.getArgument(0);
            UserEntity user = UserEntity.builder()
                    .id(UUID.fromString("e7e861df-2c3f-4304-a2b0-3b98c1ba16c8"))
                    .email("trungtin.mai1412@gmail.com")
                    .username(username)
                    .bio("A pragmatddsdsadsaic programmerss")
                    .image("https://avatars.githubusercontent.com/u/19252712?s=100&v=100")
                    .password("$2a$10$ba45PLemGgZxRXAjHkyuRuuHE0o4dmrKFcyW5a").build();
*/

            return null;
        });

        when(userRepository.findByEmail(anyString())).thenAnswer(invocation -> {
/*
            String username = invocation.getArgument(0);
            UserEntity user = UserEntity.builder()
                    .id(UUID.fromString("e7e861df-2c3f-4304-a2b0-3b98c1ba16c8"))
                    .email("trungtin.mai1412@gmail.com")
                    .username(username)
                    .bio("A pragmatddsdsadsaic programmerss")
                    .image("https://avatars.githubusercontent.com/u/19252712?s=100&v=100")
                    .password("$2a$10$ba45PLemGgZxRXAjHkyuRuuHE0o4dmrKFcyW5a").build();
*/

            return null;
        });

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setId(UUID.fromString("e7e861df-2c3f-4304-a2b0-3b98c1ba16c8"));
            return user;
        });

        return registerService.registerUser(request);

    }

    @Test
    void test_SignupNormal() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("thepianist0000")
                .password("123123123")
                .email("bluesky.silverwings@gmail.com")
                .build();

        ApiError apiError = null;
        UserResponse userResponse = null;

        try {
            userResponse = registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertEquals(userResponse.getUsername(), "thepianist0000");
        assertEquals(userResponse.getEmail(), "bluesky.silverwings@gmail.com");
    }

    @Test
    void test_SignupNullUsername() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .password("123123123")
                .email("bluesky.silverwings@gmail.com")
                .build();

        ApiError apiError = null;
        UserResponse userResponse = null;

        try {
            userResponse = registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.username.not-blank", "Username must be specified.");
    }

    @Test
    void test_SignupBlankUsername() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("")
                .password("123123123")
                .email("bluesky.silverwings@gmail.com")
                .build();

        ApiError apiError = null;
        UserResponse userResponse = null;

        try {
            userResponse = registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.username.not-blank", "Username must be specified.");
    }

    @Test
    void test_SignupExceedMaxLengthUsername() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000thepianist0000")
                .password("123123123")
                .email("bluesky.silverwings@gmail.com")
                .build();
        ApiError apiError = null;

        try {
             registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.username.size", "Username must be between 6 and 32 characters long.");
    }

    @Test
    void test_SignupNullEmail() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("thepianist00")
                .password("123123123")
                .build();

        ApiError apiError = null;
        UserResponse userResponse = null;

        try {
            userResponse = registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.username.size", "Username must be between 6 and 32 characters long.");
    }

    @Test
    void test_SignupBlankEmail() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("thepianist00")
                .password("123123123")
                .email("")
                .build();

        ApiError apiError = null;

        try {
            registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.username.size", "Username must be between 6 and 32 characters long.");
    }

    @Test
    void test_SignupInvalidEmail() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("thepianist00")
                .password("123123123")
                .email("bluesky.silverwingsgmail.com")
                .build();

        ApiError apiError = null;

        try {
            registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.email.valid", "Email is not valid.");
    }

    @Test
    void test_SignupNullPassword() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("thepianist00")
                .email("bluesky.silverwings@gmail.com")
                .build();

        ApiError apiError = null;

        try {
            registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.password.not-blank", "Password must be specified.");
    }

    @Test
    void test_SignupEmptyPassword() {
        RegisterRequest registrationDto = RegisterRequest.builder()
                .username("thepianist00")
                .email("bluesky.silverwings@gmail.com")
                .build();

        ApiError apiError = null;
        UserResponse userResponse = null;

        try {
            registerService.registerUser(registrationDto);
        } catch (InvalidDataException e) {
            apiError = e.getApiError();
        }

        assertApiError(apiError, "user.register.password.not-blank", "Password must be specified.");
    }
}
