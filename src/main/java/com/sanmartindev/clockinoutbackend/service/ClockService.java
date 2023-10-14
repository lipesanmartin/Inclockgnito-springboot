package com.sanmartindev.clockinoutbackend.service;

import com.sanmartindev.clockinoutbackend.model.Clock;
import com.sanmartindev.clockinoutbackend.repository.ClockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;
import java.util.List;

@Service
public class ClockService {

    private final ClockRepository repo;

    @Autowired
    public ClockService(ClockRepository repo) {
        this.repo = repo;
    }

    public List<Clock> findAll() {
        return repo.findAll();
    }

    public Clock findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Clock clockIn() {
        Clock record = new Clock();
        record.setClockIn(LocalTime.now());
        return repo.save(record);
    }

    public Clock clockOut(@PathVariable Long id) {
        try {
            Clock record = repo.findById(id).orElse(null);
            if (record != null) {
                record.setClockOut(LocalTime.now());
                record.setTotalTime(record.getClockIn(), record.getClockOut());
                return repo.save(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } return null;
    }

    public void delete(Long id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
