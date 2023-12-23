package overcloud.blog.repository;

import org.springframework.data.repository.query.Param;
import overcloud.blog.entity.UserEntity;

public interface IUserRepository {
    UserEntity findByUsername(String username);
}
