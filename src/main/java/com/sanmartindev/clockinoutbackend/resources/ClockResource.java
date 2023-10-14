package com.sanmartindev.clockinoutbackend.resources;

import com.sanmartindev.clockinoutbackend.models.Clock;
import com.sanmartindev.clockinoutbackend.services.ClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/clocks")
public class ClockResource {


    private final ClockService service;

    @Autowired
    public ClockResource(ClockService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Clock>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Clock> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping(value = "/last")
    public ResponseEntity<Long> findLastId() {
        return ResponseEntity.ok().body(service.findLastId());
    }


    @PostMapping()
    public ResponseEntity<Clock> create(@RequestBody String clockIn) {
        return ResponseEntity.ok().body(service.clockIn(clockIn));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Clock> clockOut(@PathVariable Long id, @RequestBody String clockOut) {
        return ResponseEntity.ok().body(service.clockOut(id, clockOut));
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
