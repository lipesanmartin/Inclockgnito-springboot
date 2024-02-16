package com.inclockgnito.controllers;

import com.inclockgnito.services.ShiftService;
import com.inclockgnito.security.Jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna a lista de todos os turnos do usuário."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar os dados de outro usuário.")
    })
    public ResponseEntity<?> findAllByUsername(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.findAllByUsername(username));
    }

    @GetMapping(value = "/{username}/last")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna o último turno do usuário."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar os dados de outro usuário.")
    })
    public ResponseEntity<?> findLastShift(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.findLastShift(username));
    }


    @GetMapping(value = "/{username}/clock-in")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna o turno criado."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar os dados de outro usuário."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "O usuário já está em um turno.")
    })
    public ResponseEntity<?> clockIn(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }

        return ResponseEntity.ok().body(service.clockIn(username));
    }

    @GetMapping(value = "/{username}/clock-out")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna o turno atualizado."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar os dados de outro usuário."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "O usuário não está em um turno.")
    })
    public ResponseEntity<?> clockOut(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }

        return ResponseEntity.ok().body(service.clockOut(username));
    }

    @GetMapping(value = "/{username}/pause")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna o turno atualizado."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar os dados de outro usuário."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "O usuário não está em um turno ou já está em pausa.")
    })
    public ResponseEntity<?> pauseIn(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.pauseIn(username));
    }

    @GetMapping(value = "/{username}/unpause")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna o turno atualizado."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar os dados de outro usuário."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "O usuário não está em um turno ou não está em pausa.")
    })
    public ResponseEntity<?> pauseOut(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.pauseOut(username));
    }

    @PutMapping(value = "/{username}/kilometers")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna o turno atualizado."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar os dados de outro usuário.")
    })
    public ResponseEntity<?> setKilometers(@PathVariable String username, @RequestBody Double kilometers, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(service.setKilometers(username, kilometers));
    }

}
