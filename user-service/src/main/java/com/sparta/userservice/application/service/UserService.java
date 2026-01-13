package com.sparta.userservice.application.service;

import com.sparta.userservice.application.dto.request.UserLoginRequest;
import com.sparta.userservice.application.dto.request.UserSignupRequest;
import com.sparta.userservice.application.dto.request.UserUpdateRequest;
import com.sparta.userservice.application.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;


public interface UserService {

    UserSignupResponse signup(UserSignupRequest request);

    UserLoginResponse login(UserLoginRequest request);

    UserInfoResponse getMyInfo(UUID userId);

    UserUpdateResponse updateMyInfo(UUID userId, UserUpdateRequest request);

    UserDeleteResponse deleteMyInfo(UUID userId);

    Page<UserListResponse> getUsers(Pageable pageable);
}
