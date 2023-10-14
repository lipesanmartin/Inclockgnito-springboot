package com.sanmartindev.clockinoutbackend.resource;

import com.sanmartindev.clockinoutbackend.model.Clock;
import com.sanmartindev.clockinoutbackend.service.ClockService;
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

    @GetMapping("/{id}")
    public ResponseEntity<Clock> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }


    @PostMapping("")
    public ResponseEntity<Clock> create() {
        return ResponseEntity.ok().body(service.clockIn());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clock> clockOut(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.clockOut(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
