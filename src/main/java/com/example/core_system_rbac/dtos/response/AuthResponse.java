package com.example.core_system_rbac.dtos.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String token;
    String refreshToken;   // Refresh Token
    @Builder.Default
    String type = "Bearer";
    UserResponse user; // Tái sử dụng UserResponse đã viết trước đó
}