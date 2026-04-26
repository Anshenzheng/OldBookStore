package com.oldbookstore.controller;

import com.oldbookstore.dto.ApiResponse;
import com.oldbookstore.dto.JwtAuthenticationResponse;
import com.oldbookstore.dto.LoginRequest;
import com.oldbookstore.dto.SignUpRequest;
import com.oldbookstore.entity.User;
import com.oldbookstore.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtAuthenticationResponse response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "登录失败: " + e.getMessage()));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            User user = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new ApiResponse(true, "用户注册成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "注册失败: " + e.getMessage()));
        }
    }
}
