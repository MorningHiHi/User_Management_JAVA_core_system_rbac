package com.example.core_system_rbac.repositories;

import com.example.core_system_rbac.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends IMasterDataRepository<User, UUID> {

    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.role.roleCode = :roleCode AND u.isDeleted = false")
    List<User> findByRoleCode(String roleCode);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Optional<User> findByEmailAndIsDeletedFalse(String email);

}