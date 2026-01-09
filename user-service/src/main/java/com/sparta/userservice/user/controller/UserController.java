package com.sparta.userservice.user.controller;

import com.sparta.userservice.user.dto.request.UserLoginRequest;
import com.sparta.userservice.user.dto.request.UserSignupRequest;
import com.sparta.userservice.user.dto.response.UserInfoResponse;
import com.sparta.userservice.user.dto.response.UserLoginResponse;
import com.sparta.userservice.user.dto.response.UserSignupResponse;
import com.sparta.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserSignupResponse> signup(@RequestBody UserSignupRequest request){
        UserSignupResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request){
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<UserInfoResponse> getMyInfo(@RequestHeader("X-User-Id") UUID userId) {
        UserInfoResponse response = userService.getMyInfo(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
