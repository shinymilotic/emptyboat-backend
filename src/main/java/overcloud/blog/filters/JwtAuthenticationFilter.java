package overcloud.blog.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import overcloud.blog.auth.service.AuthenticationProvider;
import overcloud.blog.auth.service.JwtUtils;
import overcloud.blog.exception.InvalidDataException;
import overcloud.blog.usecase.user.common.UserResMsg;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_PREFIX = "";
    private final JwtUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;
    private final ObjectsValidator validator;
    public static final String AUTHORIZE_FAILED = "authorize.failed";
    public static final String TOKEN_TIMEOUT = "authorize.token.timeout";

    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   AuthenticationProvider authenticationProvider,
                                   ObjectsValidator validator) {
        this.jwtUtils = jwtUtils;
        this.authenticationProvider = authenticationProvider;
        this.validator = validator;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException, InvalidDataException {
        Cookie[] cookies = request.getCookies();
        Optional<String> jwtToken = Optional.empty();
        Optional<String> userId = Optional.empty();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    jwtToken = Optional.of(cookie.getValue());
                }
                if (cookie.getName().equals("userId")) {
                    userId = Optional.of(cookie.getValue());
                }
            }
        }
        String url = request.getRequestURI();
        if (jwtToken.isPresent() && userId.isPresent() && !url.equals("/api/users/refreshToken")) {
            String token = jwtToken.get();
            boolean isValid;

            try {
                isValid = jwtUtils.validateToken(token);
            } catch (Exception e) {
                throw validator.fail(AUTHORIZE_FAILED);
            }

            if (!isValid) {
                throw validator.fail(TOKEN_TIMEOUT);
            }
            String email = jwtUtils.getSub(token);
            Authentication auth = authenticationProvider.getAuthentication(email);
            SecurityContextHolder.getContext().setAuthentication(auth);

//            if (tempCookie != null) {
//                response.addCookie(tempCookie);
//            }
        }

        chain.doFilter(request, response);
    }


}

