package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.dtos.request.UserCreateRequest;
import com.example.core_system_rbac.dtos.response.UserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.core_system_rbac.services.UserService;
import java.security.Principal;
import com.example.core_system_rbac.dtos.request.ChangePasswordRequest;

import jakarta.validation.Valid;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users") // Thêm version 'v1' là best practice
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated // Kích hoạt bộ kiểm tra Validation ở cấp độ class
public class UserController {

    UserService userService;

    // ==========================================
    // 1. API TẠO MỚI NGƯỜI DÙNG (CREATE)
    // Method: POST | URL: /api/v1/users
    // ==========================================
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request) {

        // Trả về mã 201 (CREATED) khi tạo tài nguyên thành công
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==========================================
    // 2. API LẤY THÔNG TIN 1 NGƯỜI DÙNG (READ)
    // Method: GET | URL: /api/v1/users/{id}
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {

        // Trả về mã 200 (OK) kèm dữ liệu
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 3. API LẤY DANH SÁCH (CÓ PHÂN TRANG)
    // Method: GET | URL: /api/v1/users?page=0&size=10
    // ==========================================
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        // Cấu hình phân trang và sắp xếp (mặc định xếp theo ngày tạo mới nhất)
        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserResponse> response = userService.getAllActiveUsers(pageable);
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 4. API XÓA TÀI KHOẢN (SOFT DELETE)
    // Method: DELETE | URL: /api/v1/users/{id}?deep=true
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "false") boolean deep) {

        // Gọi Service xử lý xóa. Biến 'deep' quyết định có xóa vét cạn quyền hay không
        userService.deleteUser(id, deep);

        // Trả về mã 204 (NO CONTENT) báo hiệu thực hiện thành công nhưng không có dữ liệu trả về
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // 5. API KHÔI PHỤC TÀI KHOẢN
    // Method: PATCH | URL: /api/v1/users/{id}/restore
    // ==========================================
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreUser(@PathVariable UUID id) {

        userService.restoreUser(id);

        // Trả về mã 200 (OK)
        return ResponseEntity.ok().build();
    }

    // ==========================================
    // 6. API ĐỔI MẬT KHẨU CHO USER ĐANG ĐĂNG NHẬP
    // Method: PATCH | URL: /api/v1/users/me/password
    // ==========================================
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeMyPassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal principal) {

        // principal.getName() sẽ tự động lấy ra "subject" từ JWT token (trong dự án của bạn là email)
        String currentUserEmail = principal.getName();

        userService.changePassword(currentUserEmail, request);

        // Trả về mã 200 (OK)
        return ResponseEntity.ok().build();
    }
}