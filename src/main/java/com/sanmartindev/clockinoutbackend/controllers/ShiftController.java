package com.sanmartindev.clockinoutbackend.controllers;

import com.sanmartindev.clockinoutbackend.security.Jwt.JwtTokenProvider;
import com.sanmartindev.clockinoutbackend.services.ShiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/shifts")
@Tag(name = "Shifts", description = "Shifts API")
public class ShiftController {

    @Autowired
    private final ShiftService service;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;


    public ShiftController(ShiftService service, JwtTokenProvider jwtTokenProvider) {
        this.service = service;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping(value = "/{username}/all")
    public ResponseEntity<?> findAllByUsername(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.findAllByUsername(username));
    }

    @GetMapping(value = "/{username}/last")
    public ResponseEntity<?> findLastShift(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.findLastShift(username));
    }


    @GetMapping(value = "/{username}/clock-in")
    public ResponseEntity<?> clockIn(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }

        return ResponseEntity.ok().body(service.clockIn(username));
    }

    @GetMapping(value = "/{username}/clock-out")
    public ResponseEntity<?> clockOut(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }

        return ResponseEntity.ok().body(service.clockOut(username));
    }

    @GetMapping(value = "/{username}/pause")
    public ResponseEntity<?> pauseIn(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.pauseIn(username));
    }

    @GetMapping(value = "/{username}/unpause")
    public ResponseEntity<?> pauseOut(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.pauseOut(username));
    }

    @PutMapping(value = "/{username}/kilometers")
    public ResponseEntity<?> setKilometers(@PathVariable String username, @RequestBody Double kilometers, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.setKilometers(username, kilometers));
    }

}
