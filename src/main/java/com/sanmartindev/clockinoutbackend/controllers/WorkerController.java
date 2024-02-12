package com.sanmartindev.clockinoutbackend.controllers;

import com.sanmartindev.clockinoutbackend.data.vo.security.PasswordVO;
import com.sanmartindev.clockinoutbackend.security.Jwt.JwtTokenProvider;
import com.sanmartindev.clockinoutbackend.services.WorkerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;


    public WorkerController(WorkerService workerService, JwtTokenProvider jwtTokenProvider) {
        this.workerService = workerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(workerService.findByUsername(username));
    }

    @PutMapping(value = "/{username}/update-wage")
    public ResponseEntity<?> updateWage(@PathVariable String username, @RequestBody Double wage, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(workerService.updateWage(username, wage));
    }

    @PutMapping(value = "/{username}/update-password")
    public ResponseEntity<?> updatePassword(@PathVariable("username") String username, @RequestBody PasswordVO data, HttpServletRequest request) {
        if (checkIfParamsIsNotNull(data))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request!");
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return workerService.changePassword(username, data);
    }

    private boolean checkIfParamsIsNotNull(PasswordVO data) {
        return data == null || data.getOldPassword() == null || data.getOldPassword().isBlank()
                || data.getNewPassword() == null || data.getNewPassword().isBlank();
    }
}
