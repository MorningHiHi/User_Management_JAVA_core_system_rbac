package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.dtos.request.LoginRequest;
import com.example.core_system_rbac.dtos.response.AuthResponse;
import com.example.core_system_rbac.dtos.response.UserResponse;
import com.example.core_system_rbac.entities.RefreshToken;
import com.example.core_system_rbac.entities.User;
import com.example.core_system_rbac.repositories.IUserRepository;
import com.example.core_system_rbac.security.CustomUserDetailsService;
import com.example.core_system_rbac.services.RefreshTokenService;
import com.example.core_system_rbac.services.UserService;
import com.example.core_system_rbac.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Khởi động server Tomcat thực tế trên cổng ngẫu nhiên
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {

    // 1. Lấy cổng động mà Tomcat đang chạy
    @LocalServerPort
    private int port;

    // 2. Khai báo RestClient - Công cụ gọi API chuẩn mới của Spring
    private RestClient restClient;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private IUserRepository userRepository;

    private LoginRequest validLoginRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // 3. Khởi tạo RestClient gắn với URL của môi trường Test
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        validLoginRequest = LoginRequest.builder()
                .email("admin@fnb.com")
                .password("123456")
                .build();

        mockUser = User.builder()
                .id(UUID.randomUUID())
                .email("admin@fnb.com")
                .build();
    }

    // ==========================================
    // TEST CASE 1: ĐĂNG NHẬP THÀNH CÔNG (Trả về 200)
    // ==========================================
    @Test
    void login_Success_Returns200AndTokens() {
        // [ARRANGE] - Chuẩn bị các kịch bản Mock
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        when(jwtUtils.generateToken(any())).thenReturn("mock_access_token_123");

        when(userRepository.findByEmailAndIsDeletedFalse("admin@fnb.com"))
                .thenReturn(Optional.of(mockUser));

        RefreshToken mockRefreshToken = RefreshToken.builder().token("mock_refresh_token_456").build();
        when(refreshTokenService.createRefreshToken(mockUser.getId()))
                .thenReturn(mockRefreshToken);

        UserResponse mockUserResponse = UserResponse.builder().email("admin@fnb.com").name("Admin FnB").build();
        when(userService.getUserByEmail("admin@fnb.com")).thenReturn(mockUserResponse);

        // [ACT] - Dùng RestClient gọi HTTP POST bằng phong cách Fluent API mới
        ResponseEntity<AuthResponse> response = restClient.post()
                .uri("/api/v1/auth/login")
                .body(validLoginRequest)
                .retrieve()
                .toEntity(AuthResponse.class); // Map thẳng kết quả ra class AuthResponse

        // [ASSERT] - Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mock_access_token_123", response.getBody().getToken());
        assertEquals("mock_refresh_token_456", response.getBody().getRefreshToken());
        assertEquals("admin@fnb.com", response.getBody().getUser().getEmail());
    }

    // ==========================================
    // TEST CASE 2: ĐẦU VÀO KHÔNG HỢP LỆ (Trả về 400)
    // ==========================================
    @Test
    void login_Fail_InvalidEmailFormat() {
        // [ARRANGE]
        LoginRequest invalidRequest = LoginRequest.builder()
                .email("admin_fnb.com") // Sai định dạng
                .password("")
                .build();

        // [ACT & ASSERT]
        // RestClient sẽ quăng ra HttpClientErrorException khi server trả về lỗi 4xx
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.class,
                () -> restClient.post()
                        .uri("/api/v1/auth/login")
                        .body(invalidRequest)
                        .retrieve()
                        .toBodilessEntity() // Chỉ lấy header/status, không parse body
        );

        // Kiểm tra xem exception văng ra có chính xác là mã 400 BAD REQUEST không
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}