package com.example.core_system_rbac.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Định dạng email không hợp lệ")
    String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    String password;
}