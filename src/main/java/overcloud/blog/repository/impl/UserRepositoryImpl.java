package overcloud.blog.repository.impl;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;
import overcloud.blog.usecase.user.common.UserResponse;
import java.util.ArrayList;
import java.util.List;
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
        jpa.enableUser(confirmToken);
    }

    @Override
    public List<UserResponse> getFollowers(UUID userId) {
        List<UserResponse> resposne = new ArrayList<>();
        
        Query query = entityManager
                .createNativeQuery("select u.id, u.email, u.username, u.bio, u.image  " +
                                "from users u " +
                                "inner join follows f " +
                                "on u.id = f.followee_id " +
                                "where f.follower_id = :userId ",
                        Tuple.class)
                .setParameter("userId", userId);

        
        final List<?> list = (List<?>) query.getResultList();
        
        list.stream().map(row -> (Tuple) row)
            .map(row -> resposne.add(new UserResponse(
                (UUID) row.get("id"),
                (String) row.get("email"),
                (String) row.get("username"),
                (String) row.get("bio"),
                (String) row.get("image"))));

        return resposne;
    }

    @Override
    public List<UserEntity> findAll(int page, int size) {
        return entityManager
                .createQuery("SELECT users FROM UserEntity users", UserEntity.class)
                .setFirstResult(page * (size - 1))
                .setMaxResults(size)
                .getResultList();

    }

    @Override
    public UserEntity findRolesByUsernname(String username) {
        return jpa.findRolesByUsernname(username);
    }

    @Override
    public List<Tuple> findProfile(String username, UUID currentUserId) {
        Query query = entityManager.createNativeQuery(
            "SELECT u.*, f2.follower_id = :currentUserId as following" +
            " FROM users u " + 
            "left join follows f2 on u.id = f2.followee_id " + 
            "where u.username = :username  ", 
        Tuple.class);

        return query.getResultList();
    }
}
