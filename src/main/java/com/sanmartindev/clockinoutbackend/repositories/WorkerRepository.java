package com.sanmartindev.clockinoutbackend.repositories;

import com.sanmartindev.clockinoutbackend.models.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long>{

    @Query("SELECT w FROM Worker w WHERE w.email = :email")
    Worker findByEmail(String email);
}
