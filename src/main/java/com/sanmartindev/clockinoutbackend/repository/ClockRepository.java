package com.sanmartindev.clockinoutbackend.repository;

import com.sanmartindev.clockinoutbackend.model.Clock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClockRepository extends JpaRepository<Clock, Long> {
}
