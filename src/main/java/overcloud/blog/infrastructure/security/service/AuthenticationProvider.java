package overcloud.blog.infrastructure.security.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import overcloud.blog.infrastructure.security.bean.SecurityUser;

import java.util.Optional;

@Component
public class AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public AuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Authentication getAuthentication(String email) {
        return Optional.ofNullable(email)
                .map(userDetailsService::loadUserByUsername)
                .map(userDetails ->
                    new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()))
                .orElse(null);
    }
}
