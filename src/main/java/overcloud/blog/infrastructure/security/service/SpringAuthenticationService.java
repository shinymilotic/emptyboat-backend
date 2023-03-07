package overcloud.blog.infrastructure.security.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.application.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SpringAuthenticationService {
  @Autowired
  private final UserRepository userRepository;
  @Autowired
  private final PasswordEncoder passwordEncoder;

  public SpringAuthenticationService(
      UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /** {@inheritDoc} */
  public Optional<SecurityUser> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .filter(auth -> !auth.getPrincipal().equals("anonymousUser"))
            .map(auth -> (String) auth.getPrincipal())
            .flatMap(ud -> Optional.ofNullable(userRepository.findByUsername(ud)))
            .map(SecurityUser::new);
  }

  /** {@inheritDoc} */
  public Optional<SecurityUser> authenticate(String email, String password) {
    return Optional.ofNullable(userRepository
        .findByEmail(email))
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

  /** {@inheritDoc} */
  public String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}
