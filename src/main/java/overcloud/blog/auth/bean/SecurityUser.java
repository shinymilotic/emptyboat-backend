package overcloud.blog.auth.bean;

import org.springframework.security.core.userdetails.UserDetails;
import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;

import java.util.Set;

public class SecurityUser implements UserDetails {
    private final UserEntity user;

    public SecurityUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    @Override
    public Set<RoleEntity> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnable();
    }
}
