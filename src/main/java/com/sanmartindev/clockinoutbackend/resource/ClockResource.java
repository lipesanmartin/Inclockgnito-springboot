package com.sanmartindev.clockinoutbackend.resource;

import com.sanmartindev.clockinoutbackend.model.ClockRecord;
import com.sanmartindev.clockinoutbackend.repository.ClockRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/clocks")
public class ClockResource {

    private final ClockRecordRepository repo;

    @Autowired
    public ClockResource(ClockRecordRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ClockRecord> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ClockRecord getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }


    @PostMapping("")
    public void clockIn() {
        ClockRecord record = new ClockRecord();
        record.setClockIn(LocalTime.now());
        repo.save(record);
    }

    @PutMapping("/{id}")
    public void clockOut(@PathVariable Long id) {
        try {
            ClockRecord record = repo.findById(id).orElse(null);
            if (record != null) {
                record.setClockOut(LocalTime.now());
                repo.save(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ;
    }


}
