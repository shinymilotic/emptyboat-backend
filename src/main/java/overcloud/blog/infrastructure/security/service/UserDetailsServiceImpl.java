package overcloud.blog.infrastructure.security.service;

import overcloud.blog.infrastructure.security.bean.SecurityUser;
import overcloud.blog.repository.jparepository.JpaUserRepository;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaUserRepository userRepository;

    public UserDetailsServiceImpl(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByEmail(email))
                .map(SecurityUser::new)
                .orElse(null);
    }
}
