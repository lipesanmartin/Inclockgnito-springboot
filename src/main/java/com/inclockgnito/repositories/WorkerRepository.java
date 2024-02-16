package com.inclockgnito.repositories;

import com.inclockgnito.models.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long>{

    @Query("SELECT w FROM Worker w WHERE LOWER(w.email)  = LOWER(:email)")
    Worker findByEmailIgnoreCase(String email);

    @Query("SELECT w FROM Worker w WHERE LOWER(w.user.username) = LOWER(:username)")
    Worker findByUsernameIgnoreCase(String username);

    @Query("SELECT COUNT(*) FROM Worker w")
    Long countAll();
}
