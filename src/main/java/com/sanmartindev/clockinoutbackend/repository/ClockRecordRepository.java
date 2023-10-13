package com.sanmartindev.clockinoutbackend.repository;

import com.sanmartindev.clockinoutbackend.model.ClockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClockRecordRepository extends JpaRepository<ClockRecord, Long> {
}
