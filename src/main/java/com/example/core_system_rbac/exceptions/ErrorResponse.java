package com.example.core_system_rbac.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;       // Mã lỗi HTTP (400, 404, 500...)
    private String error;     // Tên lỗi (Not Found, Bad Request...)
    private String message;   // Thông báo chi tiết ("Email đã tồn tại", "Không tìm thấy user"...)
    private String path;      // API nào gây ra lỗi
}