package overcloud.blog.repository;

import java.util.UUID;

public interface IUserRoleRepository {
    void assignRole(String roleName, String email);

}
