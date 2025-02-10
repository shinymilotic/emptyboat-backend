package overcloud.blog.repository.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.RefreshTokenEntity;
import overcloud.blog.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    @Modifying
    @Query(" DELETE FROM RefreshTokenEntity r WHERE r.refreshToken = :refreshToken")
    void deleteByRefreshToken(@Param("refreshToken") String refreshToken);

    @Modifying
    @Query(" DELETE FROM RefreshTokenEntity r WHERE r.userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);

    @Query(" SELECT r FROM RefreshTokenEntity r WHERE r.refreshToken = :refreshToken")
    Optional<RefreshTokenEntity> findByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query(" SELECT u FROM RefreshTokenEntity r INNER JOIN  UserEntity u ON r.userId = u.userId" +
            " WHERE r.refreshToken = :refreshToken")
    Optional<UserEntity> findUserByToken(@Param("refreshToken") String refreshToken);
}
