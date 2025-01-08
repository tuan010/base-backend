package com.littlepig.controller;

import com.littlepig.controller.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Operation(summary = "Get user list", description = "API retrieve user from db")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int pageSize){

           UserResponse userResponse = UserResponse.builder()
                   .id(1L)
                   .firstName("tuan")
                   .lastName("nguyen")
                   .userName("tuan010")
                   .gender("male")
                   .birthday(new Date())
                   .email("tuan888@dd.com")
                   .phone("098765522")
                   .build();
           UserResponse userResponse1 = UserResponse.builder()
                   .id(2L)
                   .firstName("trong")
                   .lastName("vu")
                   .userName("vdtdsds333")
                   .gender("male")
                   .birthday(new Date())
                   .email("trong3133@dd.com")
                   .phone("098765522323")
                   .build();
            List<UserResponse> userList = List.of(userResponse1, userResponse);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "user list");
            response.put("data", userList);

        return response;

    }
    @Operation(summary = "Get user detail", description = "API retrieve user detail by id")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetailById(@PathVariable("userId") Long userId){
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("tuan")
                .lastName("nguyen")
                .userName("tuan010")
                .gender("male")
                .birthday(new Date())
                .email("tuan888@dd.com")
                .phone("098765522")
                .build();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "user list");
        response.put("data", userResponse);
        return response;
    }
}
