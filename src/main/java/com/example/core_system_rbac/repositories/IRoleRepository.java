package com.example.core_system_rbac.repositories;

import com.example.core_system_rbac.entities.Permission;
import com.example.core_system_rbac.entities.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRoleRepository extends IMasterDataRepository<Role, UUID> {

    Optional<Role> findByRoleCode(String roleCode); // THÊM DÒNG NÀY

    @Query("SELECT rp.permission FROM RolePermission rp WHERE rp.role.id = :roleId")
    List<Permission> getAssignedPermissions(UUID roleId);
}