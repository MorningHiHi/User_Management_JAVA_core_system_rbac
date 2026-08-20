package com.example.core_system_rbac.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
// Kỹ thuật tối ưu JSON: Nếu trường nào bị null, tự động loại bỏ khỏi JSON trả về Client
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    String id;
    String name;
    String email;
    String phoneNumber;
    LocalDateTime birthday;
    Boolean isActive;

    // Bỏ qua password (TUYỆT ĐỐI không trả về password trong Response)
    // Trả về danh sách Role mà User này đang sở hữu
    Set<RoleResponse> roles;
}