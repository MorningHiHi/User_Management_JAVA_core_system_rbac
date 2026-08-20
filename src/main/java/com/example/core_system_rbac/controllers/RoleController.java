package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.dtos.request.RoleRequest;
import com.example.core_system_rbac.dtos.response.RoleResponse;
import com.example.core_system_rbac.services.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class RoleController {

    RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping
    public ResponseEntity<Page<RoleResponse>> getAllRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "roleCode") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(roleService.getAllActiveRoles(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "false") boolean deep) {
        roleService.deleteRole(id, deep);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRole(@PathVariable UUID id) {
        roleService.restoreRole(id);
        return ResponseEntity.ok().build();
    }
}