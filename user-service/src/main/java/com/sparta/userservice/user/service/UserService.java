package com.sparta.userservice.user.service;

import com.sparta.userservice.user.dto.request.UserLoginRequest;
import com.sparta.userservice.user.dto.request.UserSignupRequest;
import com.sparta.userservice.user.dto.response.UserLoginResponse;
import com.sparta.userservice.user.dto.response.UserSignupResponse;
import org.springframework.stereotype.Service;


public interface UserService {

    UserSignupResponse signup(UserSignupRequest request);

    UserLoginResponse login(UserLoginRequest request);
}
