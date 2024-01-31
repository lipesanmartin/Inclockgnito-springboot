package com.sanmartindev.clockinoutbackend.repositories;

import com.sanmartindev.clockinoutbackend.models.Clock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClockRepository extends JpaRepository<Clock, Long> {
    @Query("SELECT c FROM Clock c WHERE c.user.username = :username")
    List<Clock> findAllByUsername(String username);
}
