package com.sparta.userservice.api.controller;

import com.sparta.userservice.application.dto.response.*;
import com.sparta.userservice.global.response.ApiResponse;
import com.sparta.userservice.application.dto.request.UserDeleteRequest;
import com.sparta.userservice.application.dto.request.UserLoginRequest;
import com.sparta.userservice.application.dto.request.UserSignupRequest;
import com.sparta.userservice.application.dto.request.UserUpdateRequest;
import com.sparta.userservice.application.service.UserService;
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
    public ResponseEntity<ApiResponse<UserSignupResponse>> signup(@RequestBody UserSignupRequest request){
        UserSignupResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody UserLoginRequest request){
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
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
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateMyInfo(Authentication authentication,
                                                                        @RequestBody UserUpdateRequest request) {
        UUID userId = (UUID) authentication.getPrincipal();
        UserUpdateResponse response = userService.updateMyInfo(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<UserDeleteResponse>> deleteMyInfo(Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        UserDeleteResponse response = userService.deleteMyInfo(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
