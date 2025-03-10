package com.inclockgnito.controllers;

import com.inclockgnito.data.vo.security.AccountCredentialsVO;
import com.inclockgnito.services.AuthService;
import com.inclockgnito.services.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    WorkerService workerService;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNotNull(data))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        var token = authService.signin(data);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        return token;
    }

    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username,
                                       @RequestHeader("Authorization") String refreshToken) {
        if (checkIfParamsIsNotNull(username, refreshToken))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        var token = authService.refreshToken(username, refreshToken);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        return token;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsIsNotNull(data) || data.getFullname() == null || data.getFullname().isBlank() || data.getEmail() == null || data.getEmail().isBlank())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        return authService.createAccount(data);
    }

    @GetMapping(value = "/health-check")
    public ResponseEntity<?> healthCheck() {
        Long number = workerService.countAll();
        return ResponseEntity.ok().build();
    }

    private boolean checkIfParamsIsNotNull(String username, String refreshToken) {
        return refreshToken == null || refreshToken.isBlank() ||
                username == null || username.isBlank();
    }

    private boolean checkIfParamsIsNotNull(AccountCredentialsVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank()
                || data.getPassword() == null || data.getPassword().isBlank();
    }
}