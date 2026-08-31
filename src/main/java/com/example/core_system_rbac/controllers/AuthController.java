package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.dtos.request.LoginRequest;
import com.example.core_system_rbac.dtos.request.RefreshTokenRequest;
import com.example.core_system_rbac.dtos.request.UserCreateRequest;
import com.example.core_system_rbac.dtos.response.AuthResponse;
import com.example.core_system_rbac.dtos.response.UserResponse;
import com.example.core_system_rbac.entities.RefreshToken;
import com.example.core_system_rbac.entities.User;
import com.example.core_system_rbac.exceptions.TokenRefreshException;
import com.example.core_system_rbac.repositories.IUserRepository;
import com.example.core_system_rbac.services.NotificationService;
import com.example.core_system_rbac.services.RefreshTokenService;
import com.example.core_system_rbac.services.UserService;
import com.example.core_system_rbac.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthenticationManager authenticationManager;
    UserDetailsService userDetailsService;
    JwtUtils jwtUtils;
    UserService userService;
    RefreshTokenService refreshTokenService;
    IUserRepository userRepository; // Cần dùng để lấy ID user tạo Refresh Token

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtUtils.generateToken(userDetails);

        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail()).orElseThrow();

        // Tạo Refresh Token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        UserResponse userResponse = userService.getUserByEmail(request.getEmail());

        AuthResponse authResponse = AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .type("Bearer")
                .user(userResponse)
                .build();
        notificationService.notifyAdmin(userDetails.getUsername());
        return ResponseEntity.ok(authResponse);
    }

    // ==========================================
    // API LÀM MỚI ACCESS TOKEN
    // ==========================================
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        // 1. Tìm Refresh Token trong DB
        return refreshTokenService.findByToken(requestRefreshToken)
                // 2. Kiểm tra hạn sử dụng
                .map(refreshTokenService::verifyExpiration)
                // 3. Lấy User tương ứng và sinh Access Token mới
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                    String newAccessToken = jwtUtils.generateToken(userDetails);

                    return ResponseEntity.ok(AuthResponse.builder()
                            .token(newAccessToken)
                            .refreshToken(requestRefreshToken) // Trả lại refresh token cũ để client giữ
                            .type("Bearer")
                            .build());
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token không tồn tại trong hệ thống!"));
    }
}