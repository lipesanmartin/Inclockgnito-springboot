package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.models.Clock;
import com.sanmartindev.clockinoutbackend.repositories.ClockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    public Clock clockIn() { // trocar para clockin pelo servidor
        ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
        Clock record = new Clock();
        record.setClockIn(serverTime.toLocalTime());
        return repo.save(record);
    }

    public Clock clockOut(@PathVariable Long id) {
        try {
            Clock record = repo.findById(id).orElse(null);
            if (record != null) {
                ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
                record.setClockOut(serverTime.toLocalTime());
                record.setTotalTime(record.getClockIn(), record.getClockOut());
                return repo.save(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Clock pauseIn(@PathVariable Long id) {
        try {
            Clock record = repo.findById(id).orElse(null);
            if (record != null) {
                ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
                record.setPauseIn(serverTime.toLocalTime());
                return repo.save(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Clock pauseOut(@PathVariable Long id) {
        try {
            Clock record = repo.findById(id).orElse(null);
            if (record != null) {
                ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("GMT")).withNano(0);
                record.setPauseOut(serverTime.toLocalTime());
                return repo.save(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void delete(Long id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
