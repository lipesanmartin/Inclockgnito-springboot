package com.sanmartindev.clockinoutbackend.controllers;

import com.sanmartindev.clockinoutbackend.services.WorkerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
@Tag(name = "Workers", description = "Workers API")
public class WorkerController {

    @Autowired
    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(workerService.findByUsername(username));
    }

    @PutMapping(value = "/{username}/update-wage")
    public ResponseEntity<?> updateWage(@PathVariable String username, @RequestBody Double wage) {
        return ResponseEntity.ok().body(workerService.updateWage(username, wage));
    }
}
