package com.example.core_system_rbac.mappers;

import com.example.core_system_rbac.dtos.request.UserCreateRequest;
import com.example.core_system_rbac.dtos.request.UserUpdateRequest;
import com.example.core_system_rbac.dtos.response.RoleResponse;
import com.example.core_system_rbac.dtos.response.UserResponse;
import com.example.core_system_rbac.entities.User;
import com.example.core_system_rbac.entities.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * componentModel = "spring" giúp MapStruct tự động biến interface này thành một @Component (@Bean).
 * Bạn có thể nhúng (inject) nó vào UserService bằng @RequiredArgsConstructor.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    // 1. Map từ Create DTO sang Entity
    @Mapping(target = "id", ignore = true) // ID do DB tự sinh
    @Mapping(target = "userRoles", ignore = true) // Quan hệ phức tạp sẽ xử lý bằng logic ở Service
    @Mapping(target = "password", ignore = true) // Password được mã hóa thủ công ở Service
    User toEntity(UserCreateRequest request);

    // 2. Map cập nhật từ Update DTO vào Entity có sẵn
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true) // Thường không cho phép update email trực tiếp
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    void updateEntity(UserUpdateRequest request, @MappingTarget User user);

    // 3. Map từ Entity sang Response DTO
    @Mapping(target = "roles", expression = "java(mapUserRolesToRoleResponses(user.getUserRoles()))")
    UserResponse toResponse(User user);

    // Hàm hỗ trợ (Custom Mapping) để bóc tách Role từ bảng trung gian UserRole
    default Set<RoleResponse> mapUserRolesToRoleResponses(Set<UserRole> userRoles) {
        if (userRoles == null) {
            return null;
        }
        return userRoles.stream()
                .map(ur -> RoleResponse.builder()
                        .id(ur.getRole().getId())
                        .roleName(ur.getRole().getRoleName())
                        .roleCode(ur.getRole().getRoleCode())
                        .isSystem(ur.getRole().getIsSystem())
                        .isActive(ur.getRole().getIsActive())
                        .build())
                .collect(Collectors.toSet());
    }
}