package overcloud.blog.repository;

import overcloud.blog.entity.UserRole;

public interface IUserRoleRepository {
    UserRole assignRole(String roleName, String email);
    UserRole saveAndFlush(UserRole userRole);
    void delete(UserRole userRole);
}
