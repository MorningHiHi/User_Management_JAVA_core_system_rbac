package com.example.core_system_rbac.services;

import com.example.core_system_rbac.dtos.request.PermissionRequest;
import com.example.core_system_rbac.dtos.response.PermissionResponse;
import com.example.core_system_rbac.entities.Permission;
import com.example.core_system_rbac.exceptions.DuplicateResourceException;
import com.example.core_system_rbac.exceptions.ResourceNotFoundException;
import com.example.core_system_rbac.mappers.PermissionMapper;
import com.example.core_system_rbac.repositories.IPermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Transactional
    public PermissionResponse createPermission(PermissionRequest request) {
        // Validate trùng lặp tên quyền
        permissionRepository.findByNameAndIsDeletedFalse(request.getName()).ifPresent(p -> {
            throw new DuplicateResourceException("Quyền (Permission) với tên '" + request.getName() + "' đã tồn tại!");
        });

        Permission permission = permissionMapper.toEntity(request);
        permission.setIsActive(true); // Mặc định kích hoạt
        permission.setIsDeleted(false);

        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toResponse(savedPermission);
    }

    @Transactional(readOnly = true)
    public PermissionResponse getPermissionById(UUID id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Quyền với ID: " + id));
        return permissionMapper.toResponse(permission);
    }

    @Transactional(readOnly = true)
    public Page<PermissionResponse> getAllActivePermissions(Pageable pageable) {
        return permissionRepository.findByIsDeletedFalse(pageable)
                .map(permissionMapper::toResponse);
    }

    @Transactional
    public void deletePermission(UUID id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Quyền với ID: " + id));

        permissionRepository.softDelete(permission.getId());
    }

    @Transactional
    public void restorePermission(UUID id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Quyền với ID: " + id));

        permissionRepository.restore(permission.getId());
    }
}