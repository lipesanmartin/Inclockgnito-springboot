package com.sanmartindev.clockinoutbackend.controllers;

import com.sanmartindev.clockinoutbackend.data.vo.security.PasswordVO;
import com.sanmartindev.clockinoutbackend.services.WorkerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PutMapping(value = "/{username}/update-password")
    public ResponseEntity<?> updatePassword(@PathVariable("username") String username, @RequestBody PasswordVO data) {
        if (checkIfParamsIsNotNull(data))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        return workerService.changePassword(username, data);
    }

    private boolean checkIfParamsIsNotNull(PasswordVO data) {
        return data == null || data.getOldPassword() == null || data.getOldPassword().isBlank()
                || data.getNewPassword() == null || data.getNewPassword().isBlank();
    }
}
