package com.example.core_system_rbac.services;

import com.example.core_system_rbac.entities.RefreshToken;
import com.example.core_system_rbac.entities.User;
import com.example.core_system_rbac.exceptions.ResourceNotFoundException;
import com.example.core_system_rbac.exceptions.TokenRefreshException;
import com.example.core_system_rbac.repositories.IRefreshTokenRepository;
import com.example.core_system_rbac.repositories.IUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenService {

    final IRefreshTokenRepository refreshTokenRepository;
    final IUserRepository userRepository;

    @Value("${jwt.refreshExpiration:604800000}") // Mặc định 7 ngày
    Long refreshTokenDurationMs;

    @Transactional
    public RefreshToken createRefreshToken(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        // Tìm token cũ của user này, nếu chưa có thì khởi tạo một đối tượng RefreshToken mới
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .orElse(RefreshToken.builder().user(user).build());

        // Cập nhật lại chuỗi Token mới và hạn sử dụng mới
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        // Lưu xuống DB (Nếu là bản ghi cũ, Hibernate sẽ chạy UPDATE. Nếu là mới, nó chạy INSERT)
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token đã hết hạn. Vui lòng đăng nhập lại.");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}