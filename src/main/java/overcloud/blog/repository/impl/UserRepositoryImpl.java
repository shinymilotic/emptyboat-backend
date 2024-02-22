package overcloud.blog.repository.impl;

import org.springframework.stereotype.Repository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.IUserRepository;
import overcloud.blog.repository.jparepository.JpaUserRepository;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public UserEntity findByUsername(String username) {
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        return jpaUserRepository.save(entity);
    }

    @Override
    public void enableUser(String confirmToken) {
        jpaUserRepository.enableUser(confirmToken);
    }
}
