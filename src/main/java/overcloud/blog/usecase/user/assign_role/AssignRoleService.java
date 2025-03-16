package overcloud.blog.usecase.user.assign_role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import overcloud.blog.entity.RoleEntity;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.entity.UserRole;
import overcloud.blog.entity.UserRoleId;
import overcloud.blog.repository.RoleRepository;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.repository.UserRoleRepository;
import overcloud.blog.usecase.user.common.RolesRequest;
import java.util.Optional;

@Service
public class AssignRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AssignRoleService(UserRoleRepository userRoleRepository,
                             UserRepository userRepository,
                             RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public RoleAssignmentResponse assignRole(RolesRequest request, String username) {
        // List<RoleDto> roles = request.getRoles();
        // UserEntity userEntity = userRepository.findByUsername(username);
        // List<String> roleNames = roles.stream()
        //         .map(RoleDto::getRoleName)
        //         .collect(Collectors.toList());
        // List<RoleEntity> roleEntities = roleRepository.findAllByNames(roleNames);
        // Map<String, RoleEntity> roleEntitiesMap = roleEntities.stream()
        //         .collect(Collectors.toMap(RoleEntity::getName, Function.identity()));

        // for (RoleDto role : roles) {
        //     UpdateFlg updateFlg = UpdateFlg.fromInt(role.getUpdateFlg());
        //     RoleEntity roleEntity = roleEntitiesMap.get(role.getRoleName());

        //     switch (updateFlg) {
        //         case NEW -> {
        //             Optional<UserRole> userRole = assignNewRole(roleEntity, userEntity);
        //             if (userRole.isEmpty()) {
        //                 throw new InvalidDataException(ApiError.from(RoleError.ROLE_ASSIGNMENT_FAILED));
        //             }
        //         }
        //         case DELETE -> {
        //             deleteAssigedRole(roleEntity, userEntity);
        //         }
        //         default -> {
        //         }
        //     }
        // }

        return null;
    }

    private RoleAssignmentResponse toRoleAssignmentResponse(String username) {
        return RoleAssignmentResponse.builder()
                .username(username)
                .build();
    }

    private Optional<UserRole> assignNewRole(RoleEntity roleEntity, UserEntity userEntity) {
        try {
            UserRole userRole = new UserRole(new UserRoleId());
            UserRole savedEntity = userRoleRepository.saveAndFlush(userRole);
            return Optional.of(savedEntity);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void deleteAssigedRole(RoleEntity roleEntity, UserEntity userEntity) {
        UserRoleId id = new UserRoleId();
        id.setUserId(userEntity.getUserId());
        id.setRoleId(roleEntity.getRoleId());
        UserRole userRole = new UserRole();
        userRole.setId(id);

        userRoleRepository.delete(userRole);
    }
}
