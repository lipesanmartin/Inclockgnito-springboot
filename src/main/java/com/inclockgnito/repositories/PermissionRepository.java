package com.inclockgnito.repositories;

import com.inclockgnito.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT p FROM Permission p WHERE p.description = :description")
    List<Permission> findByDescription(String description);
}
