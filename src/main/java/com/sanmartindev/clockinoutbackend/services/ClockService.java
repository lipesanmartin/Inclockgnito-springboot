package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.models.Clock;
import com.sanmartindev.clockinoutbackend.repositories.ClockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    public Long findLastId() {
        List<Clock> records = repo.findAll();
        if (!records.isEmpty()) {
            return records.get(records.size() - 1).getId();
        } else {
            return null;
        }
    }

    public Clock clockIn(String clockIn) {
        Clock record = new Clock();
        record.setClockIn(clockIn);
        return repo.save(record);
    }

    public Clock clockOut(@PathVariable Long id, String clockOut) {
        try {
            Clock record = repo.findById(id).orElse(null);
            if (record != null) {
                record.setClockOut(clockOut);
                record.setTotalTime(record.getClockIn(), record.getClockOut());
                return repo.save(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } return null;
    }

    public Clock pauseIn(@PathVariable Long id, String pauseIn) {
        try {
            Clock record = repo.findById(id).orElse(null);
            if (record != null) {
                record.setPauseIn(pauseIn);
                return repo.save(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } return null;
    }

    public Clock pauseOut(@PathVariable Long id, String pauseOut) {
        try {
            Clock record = repo.findById(id).orElse(null);
            if (record != null) {
                record.setPauseOut(pauseOut);
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
