package overcloud.blog.repository;

import overcloud.blog.entity.UserRole;

import java.util.UUID;

public interface UserRoleRepository {
    UserRole assignRole(String roleName, String email);
    UserRole saveAndFlush(UserRole userRole);
    void delete(UserRole userRole);
    void deleteByUserId(UUID userId);
}
