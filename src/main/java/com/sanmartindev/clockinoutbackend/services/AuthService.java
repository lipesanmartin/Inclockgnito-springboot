package com.sanmartindev.clockinoutbackend.services;

import com.sanmartindev.clockinoutbackend.configs.SecurityConfig;
import com.sanmartindev.clockinoutbackend.data.vo.security.AccountCredentialsVO;
import com.sanmartindev.clockinoutbackend.data.vo.security.TokenVO;
import com.sanmartindev.clockinoutbackend.models.User;
import com.sanmartindev.clockinoutbackend.repositories.PermissionRepository;
import com.sanmartindev.clockinoutbackend.repositories.UserRepository;
import com.sanmartindev.clockinoutbackend.security.Jwt.JwtTokenProvider;
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


    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsVO data) {
        try {
            String username = data.getUsername();
            String password = data.getPassword();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            var user = userRepository.findByUserName(username);

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
    public ResponseEntity createAccount(AccountCredentialsVO data) {
        if (userRepository.findByUserName(data.getUsername()) != null) {
            throw new BadCredentialsException("Username already exists!");
        } else {
            User user = new User();
            user.setUserName(data.getUsername());
            String encodedPassword = securityConfig.passwordEncoder().encode(data.getPassword());
            if (encodedPassword.startsWith("{pbkdf2}"))
                user.setPassword(encodedPassword.substring("{pbkdf2}".length()));
            user.setFullName(data.getFullname());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setPermissions(permissionRepository.findAll());
            return ResponseEntity.ok().body(userRepository.save(user));
        }
    }

    @SuppressWarnings("rawtypes")
    public ResponseEntity refreshToken(String username, String refreshToken) {
        User user = userRepository.findByUserName(username);

        var tokenResponse = new TokenVO();
        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }
}