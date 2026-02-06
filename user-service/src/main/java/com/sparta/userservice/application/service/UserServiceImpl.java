package com.sparta.userservice.application.service;

import com.sparta.multi_module.common.exception.BusinessException;
import com.sparta.multi_module.common.exception.ErrorCode;
import com.sparta.multi_module.common.jwt.JwtProvider;
import com.sparta.userservice.application.dto.response.*;
import com.sparta.userservice.domain.User;
import com.sparta.userservice.domain.UserRole;
import com.sparta.userservice.domain.UserStatus;
import com.sparta.userservice.application.dto.request.UserLoginRequest;
import com.sparta.userservice.application.dto.request.UserSignupRequest;
import com.sparta.userservice.application.dto.request.UserUpdateRequest;
import com.sparta.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSignupResponse signup(UserSignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }

        User user = User.create(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                UserRole.USER
        );

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
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        String token = jwtProvider.generateToken(
                user.getUserId(),
                user.getRole().name()
        );

        return UserLoginResponse.builder()
                .accessToken(token)
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));


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
        User user =  userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        String encodedPassword = (request.getPassword() != null&& !request.getPassword().isBlank())
                ? passwordEncoder.encode(request.getPassword())
                : null;
        user.update(request.getName(), encodedPassword);

        return UserUpdateResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public UserDeleteResponse deleteMyInfo(UUID userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.delete();

        return  UserDeleteResponse.builder()
                .userId(user.getUserId())
                .status(user.getStatus().name())
                .deletedAt(user.getDeletedAt())
                .build();
    }

}
