package com.sanmartindev.clockinoutbackend.repositories;

import com.sanmartindev.clockinoutbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
