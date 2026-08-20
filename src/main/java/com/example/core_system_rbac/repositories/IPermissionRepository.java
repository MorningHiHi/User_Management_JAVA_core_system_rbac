package com.example.core_system_rbac.repositories;

import com.example.core_system_rbac.entities.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPermissionRepository extends IMasterDataRepository<Permission, UUID> {

    List<Permission> findByGroupAndIsDeletedFalse(String group);

    Optional<Permission> findByNameAndIsDeletedFalse(String name);
}