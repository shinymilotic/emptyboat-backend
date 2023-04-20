package overcloud.blog.infrastructure.security;

import overcloud.blog.infrastructure.security.service.AuthenticationProvider;
import overcloud.blog.infrastructure.security.service.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    public static final String TOKEN_PREFIX = "Token ";

    private final JwtUtils jwtUtils;

    private final AuthenticationProvider authenticationProvider;

    public JwtTokenFilter(JwtUtils jwtUtils,
                          AuthenticationProvider authenticationProvider) {
        this.jwtUtils = jwtUtils;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(TOKEN_PREFIX))
                .map(authHeader -> authHeader.substring(TOKEN_PREFIX.length()))
                .filter(jwtUtils::validateToken)
                .map(jwtUtils::getSub)
                .map(authenticationProvider::getAuthentication)
                .ifPresent(SecurityContextHolder.getContext()::setAuthentication);

        chain.doFilter(request, response);
    }
}

