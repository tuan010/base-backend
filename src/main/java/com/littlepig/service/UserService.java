package com.littlepig.service;

import com.littlepig.controller.request.UserCreationRequest;
import com.littlepig.controller.request.UserPasswordRequest;
import com.littlepig.controller.request.UserUpdateRequest;
import com.littlepig.controller.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse findByUsername(String userName);

    UserResponse findBYEmail(String email);

    void changePassword(UserPasswordRequest request);

    Long save(UserCreationRequest request);

    void delete(Long id);

    void update(UserUpdateRequest request);
}
