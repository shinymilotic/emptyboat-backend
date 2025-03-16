package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.RefreshTokenRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final JpaRefreshTokenRepository jpa;

    public RefreshTokenRepositoryImpl(JpaRefreshTokenRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpa.deleteByUserId(userId);
    }

    @Override
    public void deleteByRefreshToken(String refreshToken) {
        jpa.deleteByRefreshToken(refreshToken);
    }

    @Override
    public Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken) {
        return jpa.findByRefreshToken(refreshToken);
    }

    @Override
    public Optional<UserEntity> findUserByToken(String refreshToken) {
        return jpa.findUserByToken(refreshToken);
    }
}
