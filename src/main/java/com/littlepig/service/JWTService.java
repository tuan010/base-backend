package com.littlepig.service;

import com.littlepig.common.enums.TokenType;
import org.apache.catalina.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JWTService {
    String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String generateRefreshToken(long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String extractUsername(String token, TokenType type);
}
