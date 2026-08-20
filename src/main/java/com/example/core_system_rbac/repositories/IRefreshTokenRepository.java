package com.example.core_system_rbac.repositories;

import com.example.core_system_rbac.entities.RefreshToken;
import com.example.core_system_rbac.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(UUID userId);

    @Modifying
    void deleteByUser(User user);
}