package overcloud.blog.infrastructure.security.filters;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import overcloud.blog.infrastructure.AuthError;
import overcloud.blog.infrastructure.InvalidDataException;
import overcloud.blog.infrastructure.exceptionhandling.ApiError;
import overcloud.blog.infrastructure.security.service.AuthenticationProvider;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_PREFIX = "";
    private final JwtUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;
    public JwtAuthenticationFilter(JwtUtils jwtUtils,
                                   AuthenticationProvider authenticationProvider) {
        this.jwtUtils = jwtUtils;
        this.authenticationProvider = authenticationProvider;
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
            if (!jwtUtils.validateToken(token)) {
                throw new InvalidDataException(ApiError.from(AuthError.TOKEN_TIMEOUT));
            }
            String email = jwtUtils.getSub(token);
            Authentication auth = authenticationProvider.getAuthentication(email);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }


}

