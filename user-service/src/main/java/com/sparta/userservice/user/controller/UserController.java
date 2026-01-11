package com.sparta.userservice.user.controller;

import com.sparta.userservice.user.dto.request.UserDeleteRequest;
import com.sparta.userservice.user.dto.request.UserLoginRequest;
import com.sparta.userservice.user.dto.request.UserSignupRequest;
import com.sparta.userservice.user.dto.request.UserUpdateRequest;
import com.sparta.userservice.user.dto.response.*;
import com.sparta.userservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("signup")
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
    public ResponseEntity<Page<UserListResponse>> getUsers(@PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getMyInfo(userId));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserUpdateResponse> updateMyInfo( @RequestHeader("X-User-Id") UUID userId,
                                                            @RequestBody UserUpdateRequest request) {
        UserUpdateResponse response = userService.updateMyInfo(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<UserDeleteResponse> deleteMyInfo(@RequestHeader("X-User-Id") UUID userId,
                                                           @RequestBody UserDeleteRequest request) {
        UserDeleteResponse response = userService.deleteMyInfo(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
