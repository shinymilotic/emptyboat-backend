package overcloud.blog.usecase.user.get_roles_user;

import org.springframework.stereotype.Service;

import overcloud.blog.usecase.common.response.RestResponse;

@Service
public interface GetRolesUserService {
    RestResponse<UserRoleListResponse> getRolesUser(String username);
}
