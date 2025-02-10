package overcloud.blog.repository.impl;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.user.common.UserResponse;
import overcloud.blog.usecase.user.get_profile.GetProfileResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final JpaUserRepository jpa;
    private final EntityManager entityManager;

    public UserRepositoryImpl(JpaUserRepository jpa, EntityManager entityManager) {
        this.jpa = jpa;
        this.entityManager = entityManager;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return jpa.findByUsername(username);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return jpa.findByEmail(email);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return jpa.save(entity);
    }

    @Override
    public void enableUser(String confirmToken) {
        // jpa.enableUser(confirmToken);
    }

    @Override
    public List<UserResponse> getFollowers(UUID userId) {
        List<UserResponse> resposne = new ArrayList<>();
        
        Query query = entityManager
                .createNativeQuery("SELECT u.user_id, u.email, u.username, u.bio, u.image  " +
                                "FROM users u " +
                                "INNER JOIN follows f " +
                                "ON u.user_id = f.followee_id " +
                                "WHERE f.follower_id = :userId ",
                        Tuple.class)
                .setParameter("userId", userId);

        
        final List<?> list = (List<?>) query.getResultList();
        
        list.stream().map(row -> (Tuple) row)
            .map(row -> resposne.add(new UserResponse(
                (UUID) row.get("user_id"),
                (String) row.get("email"),
                (String) row.get("username"),
                (String) row.get("bio"),
                (String) row.get("image"))));

        return resposne;
    }

    @Override
    public List<UserEntity> findAll(int pageNumber, int itemsPerPage) {
        return entityManager
                .createQuery("SELECT users FROM UserEntity users", UserEntity.class)
                .setFirstResult((itemsPerPage * pageNumber) - itemsPerPage)
                .setMaxResults(itemsPerPage)
                .getResultList();
    }

    @Override
    public UserEntity findRolesByUsernname(String username) {
        return jpa.findRolesByUsernname(username);
    }

    @Override
    public GetProfileResponse findProfile(String username, UUID currentUserId) {
        StringBuilder sql = new StringBuilder();

        if (currentUserId != null) {
            sql.append("SELECT u.email, u.username, u.bio, u.image, f1.follower_id as following, COUNT(f2.follower_id) as followersCount ");
            sql.append(" FROM users u ");
            sql.append(" LEFT JOIN follows f1 ON u.user_id = f1.followee_id AND f1.follower_id = :currentUserId ");
            sql.append(" LEFT JOIN follows f2 on u.user_id = f2.followee_id ");
            sql.append(" WHERE u.username = :username ");
            sql.append(" GROUP BY u.user_id, following ");
        } else {
            sql.append("SELECT u.email, u.username, u.bio, u.image, COUNT(f2.follower_id) as followersCount ");
            sql.append(" FROM users u ");
            sql.append(" LEFT JOIN follows f2 on u.user_id = f2.followee_id ");
//            sql.append(" LEFT JOIN follows f3 on u.user_id = f2.follower_id ");
            sql.append(" WHERE u.username = :username ");
            sql.append(" GROUP BY u.user_id ");
        }
        Query query = entityManager.createNativeQuery(sql.toString(), Tuple.class);

        query.setParameter("username", username);
        if (currentUserId != null) {
            query.setParameter("currentUserId", currentUserId);
        }
        List<Tuple> users = query.getResultList();

        GetProfileResponse response = null;
        for (Tuple user: users) {
            response = new GetProfileResponse();
            response.setEmail(user.get("email", String.class));
            response.setUsername(user.get("username", String.class));
            response.setBio(user.get("bio", String.class));
            response.setImage(user.get("image", String.class));

            if (currentUserId != null) {
                UUID following = user.get("following", UUID.class);
                response.setFollowing(following == null ? false : true);
            } else {
                response.setFollowing(false);
            }
        }

        return response;
    }

    @Override
    public Long getUsersCount() {
        return (Long) entityManager.createNativeQuery("SELECT COUNT(*) FROM users")
                .getSingleResult();
    }

    @Override
    public void deleteUser(UUID userId) {
        jpa.deleteById(userId);
    }
}
