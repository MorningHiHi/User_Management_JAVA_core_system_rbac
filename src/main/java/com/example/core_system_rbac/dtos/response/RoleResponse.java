package com.example.core_system_rbac.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    UUID id;
    String roleName;
    String roleCode;
    String description;
    boolean isSystem;
    boolean isActive;

    // Trả về danh sách quyền (rút gọn) trực tiếp bên trong Role
    Set<PermissionResponse> permissions;
}