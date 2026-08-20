package com.example.core_system_rbac.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRequest {

    @NotBlank(message = "Tên quyền không được để trống")
    @Size(max = 100, message = "Tên quyền không vượt quá 100 ký tự")
    String name;

    @NotBlank(message = "Nhóm quyền không được để trống")
    @Size(max = 50, message = "Nhóm quyền không vượt quá 50 ký tự")
    String group;

    @Size(max = 500, message = "Mô tả không vượt quá 500 ký tự")
    String description;
}