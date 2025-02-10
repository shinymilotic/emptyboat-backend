package overcloud.blog.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface IRefreshTokenRepository {
    void deleteByUserId(UUID uuidUserId);
    void deleteByRefreshToken(@Param("refreshToken") String refreshToken);
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
    Optional<UserEntity> findUserByToken(String refreshToken);
}
