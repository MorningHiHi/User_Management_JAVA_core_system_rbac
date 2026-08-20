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
        name = "user_role",
        // Đảm bảo 1 user không bị gán trùng 1 role nhiều lần
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_role",
                        columnNames = {"user_id", "role_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole extends BaseObject {

    // --- LIÊN KẾT TỚI BẢNG USERS ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", referencedColumnName = "id", nullable = false)
    User user;

    // --- LIÊN KẾT TỚI BẢNG ROLES ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_role", referencedColumnName = "id", nullable = false)
    Role role;

    // Bạn có thể thoải mái mở rộng các thuộc tính siêu dữ liệu (metadata) tại đây sau này
    // Ví dụ:
    // @Column(name = "assigned_by")
    // String assignedBy;
}