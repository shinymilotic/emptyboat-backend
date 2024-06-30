package overcloud.blog.usecase.common.auth.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.usecase.common.auth.bean.SecurityUser;

import java.util.Optional;

@Service
public class SpringAuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SpringAuthenticationService(IUserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    public Optional<SecurityUser> getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> !auth.getPrincipal().equals("anonymousUser"))
                .map(auth -> (SecurityUser) auth.getPrincipal());
    }

    /**
     * {@inheritDoc}
     */
    public Optional<SecurityUser> authenticate(String email, String password) {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .flatMap(
                        userEntity -> {
                            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                                return Optional.of(userEntity);
                            } else {
                                return Optional.empty();
                            }
                        })
                .map(SecurityUser::new);
    }

    /**
     * {@inheritDoc}
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
