package com.example.core_system_rbac.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles") // Tên bảng thường để số nhiều
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE) // Tự động ẩn từ khóa private
public class Role extends BaseObject {

    @Column(name = "role_name", nullable = false, length = 100)
    String roleName;

    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    String roleCode;

    @Column(name = "is_system", nullable = false)
    Boolean isSystem;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Column(name = "is_delete", nullable = false)
    Boolean isDeleted;

    // --- LIÊN KẾT VỚI BẢNG TRUNG GIAN USER_ROLE ---
    // Áp dụng "Cách 2" để hệ thống linh hoạt và tối ưu hiệu suất truy vấn
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<RolePermission> rolePermissions = new HashSet<>();
}