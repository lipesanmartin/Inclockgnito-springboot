package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.configs.SecurityConfig;
import com.sanmartindev.clockinoutbackend.data.vo.security.PasswordVO;
import com.sanmartindev.clockinoutbackend.models.User;
import com.sanmartindev.clockinoutbackend.models.Worker;
import com.sanmartindev.clockinoutbackend.repositories.UserRepository;
import com.sanmartindev.clockinoutbackend.repositories.WorkerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {


    @Autowired
    private final WorkerRepository workerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public Worker save(Worker worker) {
        return workerRepository.save(worker);
    }

    public Long countAll() {
        return workerRepository.countAll();
    }

    public Worker findByUsername(String username) {
        return workerRepository.findByUsername(username);
    }

    public Worker updateWage(String username, Double wage) {
        Worker worker = workerRepository.findByUsername(username);
        worker.setHourlyWage(wage);
        return workerRepository.save(worker);
    }

    @Transactional
    public ResponseEntity<?> changePassword(String username, PasswordVO data) {
        String oldPassword = data.getOldPassword();
        String newPassword = data.getNewPassword();
        User user = userRepository.findByUserNameIgnoreCase(username);
        if (user != null) {
            if (securityConfig.passwordEncoder().matches(oldPassword, user.getPassword())) {
                String encodedPassword = securityConfig.passwordEncoder().encode(newPassword);
                if (encodedPassword.startsWith("{"))
                    user.setPassword(encodedPassword.substring(encodedPassword.indexOf("}") + 1));
                userRepository.save(user);
                return ResponseEntity.ok().body("Password changed successfully!");
            } else {
                throw new BadCredentialsException("Invalid old password!");
            }
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }

    }

}
