package com.example.core_system_rbac.mappers;

import com.example.core_system_rbac.dtos.request.RoleRequest;
import com.example.core_system_rbac.dtos.response.PermissionResponse;
import com.example.core_system_rbac.dtos.response.RoleResponse;
import com.example.core_system_rbac.entities.Role;
import com.example.core_system_rbac.entities.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rolePermissions", ignore = true) // Xử lý logic ở Service
    Role toEntity(RoleRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roleCode", ignore = true) // Không cho phép đổi mã Code
    @Mapping(target = "rolePermissions", ignore = true)
    void updateEntity(RoleRequest request, @MappingTarget Role role);

    @Mapping(target = "permissions", expression = "java(mapRolePermissionsToPermissionResponses(role.getRolePermissions()))")
    RoleResponse toResponse(Role role);

    default Set<PermissionResponse> mapRolePermissionsToPermissionResponses(Set<RolePermission> rolePermissions) {
        if (rolePermissions == null) {
            return null;
        }
        return rolePermissions.stream()
                .map(rp -> PermissionResponse.builder()
                        .id(rp.getPermission().getId().toString())
                        .name(rp.getPermission().getName())
                        .group(rp.getPermission().getGroup())
                        .description(rp.getPermission().getDescription())
                        .isActive(rp.getPermission().getIsActive())
                        .build())
                .collect(Collectors.toSet());
    }
}