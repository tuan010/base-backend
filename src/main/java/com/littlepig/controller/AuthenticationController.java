package com.littlepig.controller;

import com.littlepig.controller.request.SignInRequest;
import com.littlepig.controller.response.TokenResponse;
import com.littlepig.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-CONTROLLER")
@Tag(name = "AuthenticationController")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @Operation(summary = "Access token", description = "Get access token and refresh token by username and password")
    @PostMapping("/access-token")
    public TokenResponse getAccessToken(@RequestBody SignInRequest request) {
        log.info("Access token request");
        return authenticationService.getAccessToken(request);
//        return TokenResponse.builder()
//                .accessToken("DUMMY-ACCESS-TOKEN")
//                .refreshToken("DUMMY-REFRESH-TOKEN")
//                .build();
    }

    @Operation(summary = "Refresh token", description = "Get new access token by refresh token")
    @PostMapping("/refresh-token")
    public TokenResponse getRefreshToken(@RequestHeader String refreshToken) {
        log.info("Refresh token request, refresh token: {}", refreshToken);
        return authenticationService.getRefreshToken(refreshToken);
//        return TokenResponse.builder()
//                .accessToken("DUMMY-NEW-ACCESS-TOKEN")
//                .refreshToken("DUMMY-REFRESH-TOKEN")
//                .build();
    }
}
