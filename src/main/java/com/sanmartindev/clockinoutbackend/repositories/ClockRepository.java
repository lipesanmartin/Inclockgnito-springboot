package com.sanmartindev.clockinoutbackend.repositories;

import com.sanmartindev.clockinoutbackend.models.Clock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClockRepository extends JpaRepository<Clock, Long> {
}
