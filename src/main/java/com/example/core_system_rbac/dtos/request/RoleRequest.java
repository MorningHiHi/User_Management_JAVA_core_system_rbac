package com.example.core_system_rbac.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {

    @NotBlank(message = "Tên nhóm quyền không được để trống")
    @Size(max = 100)
    String roleName;

    @NotBlank(message = "Mã nhóm quyền không được để trống")
    @Size(max = 50)
    String roleCode;

    String description;

    // KỸ THUẬT XỬ LÝ BẢNG TRUNG GIAN (RolePermission):
    // Client chỉ cần gửi danh sách các ID của Permission muốn gán cho Role này
    Set<String> permissionIds;
}