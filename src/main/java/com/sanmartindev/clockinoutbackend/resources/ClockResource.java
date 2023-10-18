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


    @GetMapping(value = "/clock-in")
    public ResponseEntity<Clock> clockIn() { // trocar para get
        return ResponseEntity.ok().body(service.clockIn());
    }

    @GetMapping(value = "/{id}/clock-out")
    public ResponseEntity<Clock> clockOut(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.clockOut(id));
    }

    @GetMapping (value = "/{id}/pause")
    public ResponseEntity<Clock> pauseIn(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.pauseIn(id));
    }

    @GetMapping(value = "/{id}/unpause")
    public ResponseEntity<Clock> pauseOut(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.pauseOut(id));
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
