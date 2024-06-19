package overcloud.blog.usecase.common.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import overcloud.blog.core.cache.RedisUtils;
import overcloud.blog.usecase.common.auth.AuthResMsg;
import overcloud.blog.usecase.common.auth.service.AuthenticationProvider;
import overcloud.blog.usecase.common.auth.service.JwtUtils;
import overcloud.blog.usecase.common.exceptionhandling.InvalidDataException;
import overcloud.blog.usecase.common.response.RestResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_PREFIX = "";
    private final JwtUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;

    private final RedisUtils redisUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   AuthenticationProvider authenticationProvider,
                                   RedisUtils redisUtils) {
        this.jwtUtils = jwtUtils;
        this.authenticationProvider = authenticationProvider;
        this.redisUtils = redisUtils;
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
                RestResponse res = RestResponse.fail(AuthResMsg.AUTHORIZE_FAILED);
                throw new InvalidDataException(AuthResMsg.AUTHORIZE_FAILED);
            }

            if (!isValid) {
                throw new InvalidDataException(AuthResMsg.TOKEN_TIMEOUT);
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

