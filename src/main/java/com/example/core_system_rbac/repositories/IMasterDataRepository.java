package com.example.core_system_rbac.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Kế thừa trực tiếp JpaRepository.
 * Kế thừa toàn bộ tính năng CRUD mặc định và bổ sung logic Soft-Delete.
 */
@NoRepositoryBean
public interface IMasterDataRepository<T, ID> extends JpaRepository<T, ID> {

    // Lấy danh sách chưa bị xóa mềm
    List<T> findByIsDeletedFalse();

    Page<T> findByIsDeletedFalse(Pageable pageable);

    // Xóa mềm
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = true WHERE e.id = :id")
    void softDelete(@Param("id") ID id);

    // Khôi phục
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.isDeleted = false WHERE e.id = :id")
    void restore(@Param("id") ID id);
}