package com.sanmartindev.clockinoutbackend.controllers;

import com.sanmartindev.clockinoutbackend.services.ShiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/clocks")
@Tag(name = "Shifts", description = "Shifts API")
public class ShiftController {


    private final ShiftService service;

    @Autowired
    public ShiftController(ShiftService service) {
        this.service = service;
    }


    @GetMapping(value = "/{username}/all")
    public ResponseEntity<?> findAllByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(service.findAllByUsername(username));
    }

    @GetMapping(value = "/{username}/last")
    public ResponseEntity<?> findLastId(@PathVariable String username) {
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
    public ResponseEntity<?> pauseOut(@PathVariable String username) {
        return ResponseEntity.ok().body(service.pauseOut(username));
    }

    @PutMapping(value = "/{username}/kilometers")
    public ResponseEntity<?> setKilometers(@PathVariable String username, @RequestBody Double kilometers) {
        return ResponseEntity.ok().body(service.setKilometers(username, kilometers));
    }

}
