package overcloud.blog.auth.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.config.cache.RedisUtils;

import java.util.Optional;

@Component
public class AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final RedisUtils redisUtils;

    public AuthenticationProvider(UserDetailsService userDetailsService, RedisUtils redisUtils) {
        this.userDetailsService = userDetailsService;
        this.redisUtils = redisUtils;
    }

    @Transactional
    public Authentication getAuthentication(String email) {
        return Optional.ofNullable(email)
                .map(userDetailsService::loadUserByUsername)
                .map(userDetails ->
                        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()))
                .orElse(null);
    }
}
