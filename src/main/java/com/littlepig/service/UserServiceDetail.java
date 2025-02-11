package com.littlepig.service;

import com.littlepig.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceDetail {

    private final UserRepository userRepository;
    public UserDetailsService userServiceDetail(){
        return userRepository::findByUsername;
    }
}
