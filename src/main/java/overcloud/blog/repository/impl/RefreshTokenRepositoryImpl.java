package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IRefreshTokenRepository;
import overcloud.blog.repository.jparepository.JpaRefreshTokenRepository;
import overcloud.blog.usecase.user.refresh_token.RefreshTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements IRefreshTokenRepository {
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
