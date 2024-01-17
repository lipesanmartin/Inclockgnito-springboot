package com.sanmartindev.clockinoutbackend.repositories;

import com.sanmartindev.clockinoutbackend.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT p FROM Permission p WHERE p.description = :description")
    Permission findByDescription(String description);
}
