package com.sparta.userservice.api.controller;

import com.sparta.multi_module.common.response.ApiResponse;
import com.sparta.multi_module.common.security.DefaultAuthenticatedUser;
import com.sparta.userservice.application.dto.response.*;
import com.sparta.userservice.application.dto.request.UserLoginRequest;
import com.sparta.userservice.application.dto.request.UserSignupRequest;
import com.sparta.userservice.application.dto.request.UserUpdateRequest;
import com.sparta.userservice.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원 관리 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 이름으로 회원가입")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효성 검증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이메일 중복")
    })
    @PostMapping("signup")
    public ResponseEntity<ApiResponse<UserSignupResponse>> signup(@RequestBody  @Valid UserSignupRequest request) {
        UserSignupResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인 후 JWT 토큰 발급")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공, JWT 반환"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "비밀번호 불일치"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody @Valid UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    @Operation(summary = "전체 사용자 목록 조회", description = "ADMIN 권한 필요. 페이지네이션 지원")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserListResponse>> getUsers(@ParameterObject @PageableDefault(size = 30) Pageable pageable) {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    @Operation(summary = "내 정보 조회", description = "JWT 토큰 기반 본인 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        return ResponseEntity.ok(userService.getMyInfo(principal.getUserId()));
    }

    @Operation(summary = "내 정보 수정", description = "이름, 비밀번호 변경")
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal,
            @RequestBody UserUpdateRequest request) {
        UserUpdateResponse response = userService.updateMyInfo(principal.getUserId(), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "회원 탈퇴", description = "소프트 삭제 처리")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<UserDeleteResponse>> deleteMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal DefaultAuthenticatedUser principal) {
        UserDeleteResponse response = userService.deleteMyInfo(principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
