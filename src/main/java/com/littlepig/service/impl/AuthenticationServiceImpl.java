package com.littlepig.service.impl;

import com.littlepig.controller.request.SignInRequest;
import com.littlepig.controller.response.TokenResponse;
import com.littlepig.repository.UserRepository;
import com.littlepig.service.AuthenticationService;
import com.littlepig.service.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        log.info("Get access token");
        try {
            // check user co ton tai hay ko
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                    request.getPassword()));
            //luu user cho luot request tiep theo
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            log.error("Login fail, message={}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }
        //login xong thi lay thong tin user
        var user = userRepository.findByUsername(request.getUsername());


        String accessToken = jwtService.generateAccessToken(user.getId(), request.getUsername(), user.getAuthorities());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), request.getUsername(), user.getAuthorities());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String request) {
        return null;
    }
}
