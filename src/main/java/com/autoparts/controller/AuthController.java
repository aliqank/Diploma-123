package com.autoparts.controller;

import com.autoparts.dto.user.UserCreateDto;
import com.autoparts.security.AuthRequest;
import com.autoparts.security.AuthResponse;
import com.autoparts.security.JwtProvider;
import com.autoparts.serivce.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Validated UserCreateDto userCreateDto){
        userService.create(userCreateDto);
        return new ResponseEntity<>("User created successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        var user = userService.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (Objects.nonNull(user)) {
            String token = jwtProvider.generateToken(user);
            return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
