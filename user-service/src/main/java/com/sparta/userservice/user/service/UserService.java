package com.sparta.userservice.user.service;

import com.sparta.userservice.user.dto.request.UserLoginRequest;
import com.sparta.userservice.user.dto.request.UserSignupRequest;
import com.sparta.userservice.user.dto.request.UserUpdateRequest;
import com.sparta.userservice.user.dto.response.UserInfoResponse;
import com.sparta.userservice.user.dto.response.UserLoginResponse;
import com.sparta.userservice.user.dto.response.UserSignupResponse;
import com.sparta.userservice.user.dto.response.UserUpdateResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface UserService {

    UserSignupResponse signup(UserSignupRequest request);

    UserLoginResponse login(UserLoginRequest request);

    UserInfoResponse getMyInfo(UUID userId);

    UserUpdateResponse updateMyInfo(UUID userId, UserUpdateRequest request);
}
