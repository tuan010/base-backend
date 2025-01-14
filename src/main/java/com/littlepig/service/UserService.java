package com.littlepig.service;

import com.littlepig.controller.request.UserCreationRequest;
import com.littlepig.controller.request.UserPasswordRequest;
import com.littlepig.controller.request.UserUpdateRequest;
import com.littlepig.controller.response.UserPageResponse;
import com.littlepig.controller.response.UserResponse;

import java.util.List;

public interface UserService {
    UserPageResponse findAll(String keyword, String sort, int page, int size);

    UserResponse findById(Long id);

    UserResponse findByUsername(String userName);

    UserResponse findByEmail(String email);

    void changePassword(UserPasswordRequest request);

    Long save(UserCreationRequest request);

    void delete(Long id);

    void update(UserUpdateRequest request);
}
