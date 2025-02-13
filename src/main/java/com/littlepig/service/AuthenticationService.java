package com.littlepig.service;

import com.littlepig.controller.request.SignInRequest;
import com.littlepig.controller.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(String request);

}
