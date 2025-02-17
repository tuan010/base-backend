package com.littlepig.service.impl;

import com.littlepig.common.enums.TokenType;
import com.littlepig.exception.InvalidDataException;
import com.littlepig.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.littlepig.common.enums.TokenType.ACCESS_TOKEN;
import static com.littlepig.common.enums.TokenType.REFRESH_TOKEN;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JWTServiceImpl implements JWTService {

    @Value("${jwt.expiryMin}")
    private long expiryMinutes;
    @Value("${jwt.expiryDay}")
    private long expiryDay;
    @Value("${jwt.accessKey}")
    private String accessKey;
    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Override
    public String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generate access token for user {} with authorities {}", userId, authorities);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);

        return generateToken(claims, username);
    }

    @Override
    public String generateRefreshToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generate refresh token for user {} with authorities {}", userId, authorities);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", authorities);

        return generateFreshToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        log.info("Extract username from token {} with type {}", token, type);

        return extractClaims(type, token, Claims::getSubject);
    }

    private <T> T extractClaims(TokenType type, String token, Function<Claims, T> claimsExtractor){
        final Claims claims = extractAllClaims(type, token);
        return claimsExtractor.apply(claims);
    }

    private Claims extractAllClaims(TokenType type, String token) {
        try {
            return Jwts.parser().setSigningKey(accessKey).parseClaimsJws(token).getBody();
        } catch (SignatureException | ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied!, error: "+e.getMessage());
        }
    }

    private String generateToken(Map<String, Object> claims, String username){
        log.info("Generate access token for user {} with name {}", username, claims);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiryMinutes))
                .signWith(getKey(ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateFreshToken(Map<String, Object> claims, String username){
        log.info("Generate access token for user {} with name {}", username, claims);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDay))
                .signWith(getKey(REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type){
        switch (type){
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> {
                throw new InvalidDataException("Invalid token type");
            }
        }
    }
}
