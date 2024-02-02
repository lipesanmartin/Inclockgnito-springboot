package com.sanmartindev.clockinoutbackend.controllers;

import com.sanmartindev.clockinoutbackend.exceptions.ClockInvalidOperationException;
import com.sanmartindev.clockinoutbackend.models.Clock;
import com.sanmartindev.clockinoutbackend.services.ClockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/clocks")
@Tag(name = "Clocks", description = "Clocks API")
public class ClockController {


    private final ClockService service;

    @Autowired
    public ClockController(ClockService service) {
        this.service = service;
    }


    @GetMapping(value = "/{username}/all")
    public ResponseEntity<List<Clock>> findAllByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(service.findAllByUsername(username));
    }

    @GetMapping(value = "/{username}/last")
    public ResponseEntity<Long> findLastId(@PathVariable String username) {
        return ResponseEntity.ok().body(service.findLastId(username));
    }


    @GetMapping(value = "/{username}/clock-in")
    public ResponseEntity<?> clockIn(@PathVariable String username) {
        return ResponseEntity.ok().body(service.clockIn(username));
    }

    @GetMapping(value = "/{username}/clock-out")
    public ResponseEntity<?> clockOut(@PathVariable String username) {
        return ResponseEntity.ok().body(service.clockOut(username));
    }

    @GetMapping (value = "/{username}/pause")
    public ResponseEntity<?> pauseIn(@PathVariable String username) {
        return ResponseEntity.ok().body(service.pauseIn(username));
    }

    @GetMapping(value = "/{username}/unpause")
    public ResponseEntity<Clock> pauseOut(@PathVariable String username) {
        return ResponseEntity.ok().body(service.pauseOut(username));
    }

}
