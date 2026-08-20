package com.example.core_system_rbac.repositories;

import com.example.core_system_rbac.entities.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IRolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId")
    void deleteByRoleId(UUID roleId);
}