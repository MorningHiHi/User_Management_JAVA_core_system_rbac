package com.example.core_system_rbac.repositories;

import com.example.core_system_rbac.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, UUID> {

    // Spring Data JPA sẽ tự động tạo câu SQL DELETE WHERE user_id = ?
    void deleteByUserId(UUID userId);
}