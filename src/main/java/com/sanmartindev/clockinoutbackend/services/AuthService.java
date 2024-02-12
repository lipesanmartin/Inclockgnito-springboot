package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.configs.SecurityConfig;
import com.sanmartindev.clockinoutbackend.data.vo.security.AccountCredentialsVO;
import com.sanmartindev.clockinoutbackend.data.vo.security.TokenVO;
import com.sanmartindev.clockinoutbackend.models.User;
import com.sanmartindev.clockinoutbackend.models.Worker;
import com.sanmartindev.clockinoutbackend.repositories.PermissionRepository;
import com.sanmartindev.clockinoutbackend.repositories.UserRepository;
import com.sanmartindev.clockinoutbackend.repositories.WorkerRepository;
import com.sanmartindev.clockinoutbackend.security.Jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private WorkerRepository workerRepository;


    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsVO data) {
        try {
            String username = data.getUsername();
            String password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var user = userRepository.findByUserNameIgnoreCase(username);
            var tokenResponse = new TokenVO();
            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    public ResponseEntity createAccount(AccountCredentialsVO data) {
        if (userRepository.findByUserNameIgnoreCase(data.getUsername()) != null) {
            throw new BadCredentialsException("Username already exists!");
        }
        if (workerRepository.findByEmailIgnoreCase(data.getEmail()) != null) {
            throw new BadCredentialsException("Email already in use!");
        } else {
            User user = new User();
            Worker worker = new Worker();
            user.setUserName(data.getUsername());
            String encodedPassword = securityConfig.passwordEncoder().encode(data.getPassword());
            if (encodedPassword.startsWith("{"))
                user.setPassword(encodedPassword.substring(encodedPassword.indexOf("}") + 1));
            worker.setFullName(data.getFullname());
            worker.setEmail(data.getEmail());
            if (data.getHourlyWage() != null) {
                worker.setHourlyWage(data.getHourlyWage());
            } else {
                worker.setHourlyWage(0.0);
            }
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setPermissions(permissionRepository.findByDescription("COMMON USER"));
            userRepository.save(user);
            User userSaved = userRepository.findByUserNameIgnoreCase(data.getUsername());
            worker.setUser(userSaved);
            workerService.save(worker);
            return ResponseEntity.ok().body("Account created successfully!");
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshToken) {
        User user = userRepository.findByUserNameIgnoreCase(username);
        var tokenResponse = new TokenVO();
        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }
}