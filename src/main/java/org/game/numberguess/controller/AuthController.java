package org.game.numberguess.controller;

import jakarta.validation.Valid;
import org.game.numberguess.dto.request.RegisterRequest;
import org.game.numberguess.dto.request.LoginRequest;
import org.game.numberguess.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.game.numberguess.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        userService.usernameExists(request.getUsername());
        userService.register(request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        LoginResponse response = userService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}