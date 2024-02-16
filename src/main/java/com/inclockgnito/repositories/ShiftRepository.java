package com.inclockgnito.repositories;

import com.inclockgnito.models.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    @Query("SELECT s FROM Shift s WHERE s.user.username = :username ORDER BY s.id ASC")
    List<Shift> findAllByUsername(String username);
}
