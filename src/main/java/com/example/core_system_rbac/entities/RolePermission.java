package com.example.core_system_rbac.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "role_permissions",
        // Ngăn chặn việc cấp trùng 1 quyền cho cùng 1 role
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_role_permission",
                        columnNames = {"role_id", "permission_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission extends BaseObject {

    // --- LIÊN KẾT TỚI BẢNG ROLES ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_roles", referencedColumnName = "id", nullable = false)
    Role role;

    // --- LIÊN KẾT TỚI BẢNG PERMISSIONS ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_permission", referencedColumnName = "id", nullable = false)
    Permission permission;
}