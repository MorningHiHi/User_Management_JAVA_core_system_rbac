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
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseObject {

    @Column(name = "name", nullable = false, unique = true, length = 100)
    String name;

    // Tránh dùng tên cột là 'group' vì đây là từ khóa nhạy cảm trong SQL
    @Column(name = "\"group\"", nullable = false, length = 50)
    String group;

    @Column(name = "description", length = 500)
    String description;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;

    @Column(name = "is_delete", nullable = false)
    Boolean isDeleted;

    // --- LIÊN KẾT ĐẾN BẢNG TRUNG GIAN ROLE_PERMISSION ---
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<RolePermission> rolePermissions = new HashSet<>();
}