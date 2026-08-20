package com.example.core_system_rbac.mappers;

import com.example.core_system_rbac.dtos.request.PermissionRequest;
import com.example.core_system_rbac.dtos.response.PermissionResponse;
import com.example.core_system_rbac.entities.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @Mapping(target = "id", ignore = true)
    Permission toEntity(PermissionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true) // Tên quyền (action) thường mang tính định danh hệ thống, hạn chế sửa
    void updateEntity(PermissionRequest request, @MappingTarget Permission permission);

    PermissionResponse toResponse(Permission permission);
}