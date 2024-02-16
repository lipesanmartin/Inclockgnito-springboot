package com.inclockgnito.controllers;

import com.inclockgnito.data.vo.security.PasswordVO;
import com.inclockgnito.security.Jwt.JwtTokenProvider;
import com.inclockgnito.services.WorkerService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping(value = "/all")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna a lista de todos os usuários."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar os dados de todos os usuários.")
    })
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        if (!jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar os dados de todos os usuários.");
        }
        return ResponseEntity.ok().body(workerService.findAll());
    }

    @GetMapping(value = "/{username}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna os dados do usuário."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar os dados de outro usuário.")
    })
    public ResponseEntity<?> findByUsername(@PathVariable String username, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(workerService.findByUsernameIgnoreCase(username));
    }

    @PutMapping(value = "/{username}/update-wage")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna os dados do usuário atualizados."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar os dados de outro usuário.")
    })
    public ResponseEntity<?> updateWage(@PathVariable String username, @RequestBody Double wage, HttpServletRequest request) {
        String jwtUsername = jwtTokenProvider.getJwtUsername(jwtTokenProvider.resolveToken(request));
        if (!jwtUsername.equals(username) && !jwtTokenProvider.hasRole(jwtTokenProvider.resolveToken(request), "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para alterar os dados de outro usuário.");
        }
        return ResponseEntity.ok().body(workerService.updateWage(username, wage));
    }

    @PutMapping(value = "/{username}/update-password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Retorna os dados do usuário atualizados."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Você não tem permissão para alterar os dados de outro usuário.")
    })
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
