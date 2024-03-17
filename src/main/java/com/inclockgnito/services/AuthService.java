package com.inclockgnito.services;

import com.inclockgnito.configs.SecurityConfig;
import com.inclockgnito.data.vo.security.AccountCredentialsVO;
import com.inclockgnito.data.vo.security.TokenVO;
import com.inclockgnito.exceptions.UsernameAlreadyInUseException;
import com.inclockgnito.models.User;
import com.inclockgnito.models.Worker;
import com.inclockgnito.repositories.PermissionRepository;
import com.inclockgnito.repositories.UserRepository;
import com.inclockgnito.repositories.WorkerRepository;
import com.inclockgnito.security.Jwt.JwtTokenProvider;
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


    public ResponseEntity<?> signin(AccountCredentialsVO data) {
        try {
            String username = data.getUsername();
            String password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByUserNameIgnoreCase(username);
            var tokenResponse = new TokenVO();
            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(user.getUsername(), user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    @Transactional
    public ResponseEntity<?> createAccount(AccountCredentialsVO data) {
        if (userRepository.findByUserNameIgnoreCase(data.getUsername()) != null) {
            throw new UsernameAlreadyInUseException("Username already in use!");
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
            user.setPermissions(permissionRepository.findByDescription("COMMON_USER"));
            userRepository.save(user);
            User userSaved = userRepository.findByUserNameIgnoreCase(data.getUsername());
            worker.setUser(userSaved);
            workerService.save(worker);
            return ResponseEntity.ok().body("Account created successfully!");
        }
    }

    public ResponseEntity<?> refreshToken(String username, String refreshToken) {
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