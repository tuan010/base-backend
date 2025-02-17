package com.littlepig.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.littlepig.common.enums.TokenType;
import com.littlepig.service.JWTService;
import com.littlepig.service.UserServiceDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j(topic = "CUSTOMIZE-REQUEST-FILTER")
@RequiredArgsConstructor
public class CustomizeRequestFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserServiceDetail userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("{} {}", request.getMethod(), request.getRequestURI());
        //get key token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.substring(7);
            log.info("Bearer header: {}", authHeader.substring(0, 20));

            //neu khong lay duoc user thi se tra ra loi
            String username = "";
            try {
                username = jwtService.extractUsername(authHeader, TokenType.ACCESS_TOKEN);
                log.info("username: {}", username);
            } catch (Exception e) {
                log.error("Access Denied: message={}, ", e.getMessage());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(errorResponse(e.getMessage()));
                return;
            }

            // kiem tra xem user co hop le ko

            UserDetails userDetails = userDetailsService.userServiceDetail().loadUserByUsername(username);

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String errorResponse(String message) {
        try {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(new Date());
            errorResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            errorResponse.setError("Forbidden");
            errorResponse.setMessage(message);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(errorResponse);
        } catch (Exception e) {
            return "";
        }
    }

    @Getter
    @Setter
    private class ErrorResponse {
        private Date timestamp;
        private int status;
        private String error;
        private String message;
    }
}
