package com.sparta.userservice.user.service;

import com.sparta.userservice.user.domain.User;
import com.sparta.userservice.user.domain.UserRole;
import com.sparta.userservice.user.dto.request.UserLoginRequest;
import com.sparta.userservice.user.dto.request.UserSignupRequest;
import com.sparta.userservice.user.dto.response.UserLoginResponse;
import com.sparta.userservice.user.dto.response.UserSignupResponse;
import com.sparta.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    //공통 메서드
    private void validateSignup(UserSignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일");
        }
    }
}
