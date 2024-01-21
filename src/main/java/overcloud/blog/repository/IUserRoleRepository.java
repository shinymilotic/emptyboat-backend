package overcloud.blog.repository;

public interface IUserRoleRepository {
    void assignRole(String roleName, String email);

}
