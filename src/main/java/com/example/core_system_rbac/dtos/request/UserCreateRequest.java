package com.example.core_system_rbac.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100)
    String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 32, message = "Mật khẩu phải từ 6 đến 32 ký tự")
    String password;

    @Size(max = 20)
    String phoneNumber;

    Boolean isActive;

    LocalDateTime birthday;

    // KỸ THUẬT XỬ LÝ BẢNG TRUNG GIAN (UserRole):
    // Nhận danh sách ID các Role để gán cho User khi tạo tài khoản mới
    Set<UUID> roleIds;
}