package overcloud.blog.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import overcloud.blog.infrastructure.auth.AuthError;
import overcloud.blog.infrastructure.auth.service.AuthenticationProvider;
import overcloud.blog.infrastructure.auth.service.JwtUtils;
import overcloud.blog.infrastructure.cache.RedisUtils;
import overcloud.blog.infrastructure.exceptionhandling.InvalidDataException;

import java.io.IOException;
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

        Optional<String> tokenOptional = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(TOKEN_PREFIX))
                .map(authHeader -> authHeader.substring(TOKEN_PREFIX.length()));

        if (tokenOptional.isPresent()) {
            String token = tokenOptional.get();
            boolean isValid;

            try {
                isValid = jwtUtils.validateToken(token);
            } catch (Exception e) {
                throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
            }

            if (!isValid) {
                throw new InvalidDataException(AuthError.TOKEN_TIMEOUT);
            }
            String email = jwtUtils.getSub(token);
            Authentication auth = authenticationProvider.getCachedAuthentication(email);

            if (auth == null) {
                throw new InvalidDataException(AuthError.AUTHORIZE_FAILED);
//                auth = authenticationProvider.getAuthentication(email);
            }

//            if (auth != null) {
//                redisUtils.set(email, auth);
//            }
//
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }


}

