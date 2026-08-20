package com.example.core_system_rbac.controllers;

import com.example.core_system_rbac.dtos.request.PermissionRequest;
import com.example.core_system_rbac.dtos.response.PermissionResponse;
import com.example.core_system_rbac.services.PermissionService;
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
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.createPermission(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable UUID id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PermissionResponse>> getAllPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "group") String sortBy, // Thường permission được sort theo Group Module
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(permissionService.getAllActivePermissions(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restorePermission(@PathVariable UUID id) {
        permissionService.restorePermission(id);
        return ResponseEntity.ok().build();
    }
}