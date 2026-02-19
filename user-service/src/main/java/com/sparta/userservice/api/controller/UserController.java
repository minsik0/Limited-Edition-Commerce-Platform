package com.sparta.userservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.multi_module.common.security.DefaultAuthenticatedUser;
import com.sparta.userservice.application.dto.response.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserListResponse>> getUsers(@PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        return ResponseEntity.ok(userService.getMyInfo(principal.getUserId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateMyInfo(@AuthenticationPrincipal DefaultAuthenticatedUser principal,
                                                                        @RequestBody UserUpdateRequest request) {

        UserUpdateResponse response = userService.updateMyInfo(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<UserDeleteResponse>> deleteMyInfo(@AuthenticationPrincipal DefaultAuthenticatedUser principal) {

        UserDeleteResponse response = userService.deleteMyInfo(principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
