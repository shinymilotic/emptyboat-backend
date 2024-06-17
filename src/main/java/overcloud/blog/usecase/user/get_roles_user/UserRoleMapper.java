package overcloud.blog.usecase.user.get_roles_user;


import org.springframework.stereotype.Component;
import overcloud.blog.entity.RoleEntity;

import java.util.Collection;

@Component
public class UserRoleMapper {

    public UserRoleListResponse toUserRoleListResponse(Collection<RoleEntity> roles) {
//        List<RoleResponse> roleResponseList = roles.stream()
//                .map(this::toUserRoleResponse)
//                .toList();
//
//        return RoleListResponse.builder()
//                .roleList(roleResponseList)
//                .build();
        return null;
    }

    public UserRoleResponse toUserRoleResponse(RoleEntity roleEntity, boolean checked) {
        return UserRoleResponse.builder()
                .roleId(roleEntity.getId().toString())
                .roleName(roleEntity.getName())
                .checked(checked)
                .build();
    }

}
