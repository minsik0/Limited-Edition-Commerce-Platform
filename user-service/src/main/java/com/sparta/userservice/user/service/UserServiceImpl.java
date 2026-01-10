package com.sparta.userservice.user.service;

import com.sparta.userservice.user.domain.User;
import com.sparta.userservice.user.domain.UserRole;
import com.sparta.userservice.user.domain.UserStatus;
import com.sparta.userservice.user.dto.request.UserDeleteRequest;
import com.sparta.userservice.user.dto.request.UserLoginRequest;
import com.sparta.userservice.user.dto.request.UserSignupRequest;
import com.sparta.userservice.user.dto.request.UserUpdateRequest;
import com.sparta.userservice.user.dto.response.*;
import com.sparta.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserSignupResponse signup(UserSignupRequest request) {
        validateSignup(request);

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);

        return UserSignupResponse.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        return UserLoginResponse.builder()
                .accessToken("Mock-token")
                .userId(user.getUserId())
                .role(user.getRole().name())
                .build();

    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserListResponse> getUsers(Pageable pageable) {
        return userRepository.findAllByStatus(UserStatus.ACTIVE, pageable)
                .map(user -> UserListResponse.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .name(user.getName())
                        .role(user.getRole().name())
                        .build()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo(UUID userId) {
        User user = getActiveUser(userId);


        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public UserUpdateResponse updateMyInfo(UUID userId, UserUpdateRequest request) {
        User user =  getActiveUser(userId);
        user.update(request.getName(), request.getPassword());

        return UserUpdateResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserDeleteResponse deleteMyInfo(UUID userId, UserDeleteRequest request) {
        User user = getActiveUser(userId);
        user.delete();

        return  UserDeleteResponse.builder()
                .userId(user.getUserId())
                .status(user.getStatus().name())
                .deletedAt(user.getDeletedAt())
                .build();
    }

    //공통 메서드
    private void validateSignup(UserSignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일");
        }
    }

    private User  getActiveUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));

        if(user.getStatus()== UserStatus.DELETED) {
            throw new IllegalArgumentException("탈퇴한 회원");
        }
        return user;
    }
}
