package com.example.core_system_rbac.services;

import com.example.core_system_rbac.dtos.request.RoleRequest;
import com.example.core_system_rbac.dtos.response.RoleResponse;
import com.example.core_system_rbac.entities.Permission;
import com.example.core_system_rbac.entities.Role;
import com.example.core_system_rbac.entities.RolePermission;
import com.example.core_system_rbac.exceptions.DuplicateResourceException;
import com.example.core_system_rbac.exceptions.ResourceNotFoundException;
import com.example.core_system_rbac.mappers.RoleMapper;
import com.example.core_system_rbac.repositories.IPermissionRepository;
import com.example.core_system_rbac.repositories.IRolePermissionRepository;
import com.example.core_system_rbac.repositories.IRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    IRoleRepository roleRepository;
    IPermissionRepository permissionRepository;
    IRolePermissionRepository rolePermissionRepository;
    RoleMapper roleMapper;

    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        // Kiểm tra mã RoleCode duy nhất
        roleRepository.findByRoleCode(request.getRoleCode()).ifPresent(r -> {
            throw new DuplicateResourceException("Mã nhóm quyền (Role Code) '" + request.getRoleCode() + "' đã tồn tại!");
        });

        Role role = roleMapper.toEntity(request);
        role.setIsSystem(false); // Mặc định role tự tạo không phải là System Role
        role.setIsActive(true);
        role.setIsDeleted(false);

        // Xử lý mapping RolePermission nếu Client truyền lên danh sách permissionIds
        Set<RolePermission> rolePermissions = new HashSet<>();
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            for (String pIdStr : request.getPermissionIds()) {
                UUID permissionId = UUID.fromString(pIdStr);
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission ID không tồn tại: " + permissionId));

                rolePermissions.add(RolePermission.builder()
                        .role(role)
                        .permission(permission)
                        .build());
            }
        }
        role.setRolePermissions(rolePermissions);

        Role savedRole = roleRepository.save(role);
        return roleMapper.toResponse(savedRole);
    }

    @Transactional(readOnly = true)
    public RoleResponse getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhóm quyền với ID: " + id));
        return roleMapper.toResponse(role);
    }

    @Transactional(readOnly = true)
    public Page<RoleResponse> getAllActiveRoles(Pageable pageable) {
        return roleRepository.findByIsDeletedFalse(pageable)
                .map(roleMapper::toResponse);
    }

    @Transactional
    public void deleteRole(UUID id, boolean isDeepDelete) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhóm quyền với ID: " + id));

        // QUY TẮC BẤT KHẢ XÂM PHẠM: Không cho phép xóa Role cốt lõi của hệ thống
        if (role.getIsSystem()) {
            throw new IllegalArgumentException("Bất khả xâm phạm: Không thể xóa nhóm quyền hệ thống (System Role)!");
        }

        if (isDeepDelete) {
            // Xóa sạch các liên kết giữa Role và Permission trong bảng trung gian
            rolePermissionRepository.deleteByRoleId(role.getId());
        }

        roleRepository.softDelete(id);
    }

    @Transactional
    public void restoreRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhóm quyền với ID: " + id));

        roleRepository.restore(role.getId());
    }
}