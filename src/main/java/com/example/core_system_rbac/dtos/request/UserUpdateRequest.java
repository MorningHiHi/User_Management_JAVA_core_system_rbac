package com.example.core_system_rbac.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100)
    String name;

    @Size(max = 20)
    String phoneNumber;

    LocalDateTime birthday;

    // Cho phép Admin khóa/mở khóa tài khoản khi update
    Boolean isActive;

    // Danh sách ID quyền mới muốn cập nhật lại cho User
    Set<String> roleIds;
}