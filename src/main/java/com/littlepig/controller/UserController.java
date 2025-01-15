package com.littlepig.controller;

import com.littlepig.controller.request.UserCreationRequest;
import com.littlepig.controller.request.UserPasswordRequest;
import com.littlepig.controller.request.UserUpdateRequest;
import com.littlepig.controller.response.UserPageResponse;
import com.littlepig.controller.response.UserResponse;
import com.littlepig.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
@ToString
@Validated
public class UserController {
    private final UserService userService;
    @Operation(summary = "Get user list", description = "API retrieve user from db")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String sort,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int pageSize){
            log.info("Get user list");
            UserPageResponse userPageResponse = userService.findAll(keyword, sort, page, pageSize);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "user list");
            response.put("data", userPageResponse);

        return response;

    }
    @Operation(summary = "Get user detail", description = "API retrieve user detail by id")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetailById(@PathVariable("userId") @Min(value = 1, message = "userId must be equals or greater than 1") Long userId){
        log.info("Get user detail : {}", userId);
            UserResponse  userResponse =  userService.findById(userId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "user list");
        response.put("data", userResponse);
        return response;
    }

    @Operation(summary = "Create user", description = "API add new user to db")
    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreationRequest request){
            log.info("Create user: {}", request);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.CREATED.value());
        response.put("message", "user created successfully");
        response.put("data", userService.save(request));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Update user", description = "API update an user to db")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody @Valid UserUpdateRequest request){
        log.info("Update user: {}", request);
        userService.update(request);
        //get user after update
        UserResponse userResponse = userService.findById(request.getId());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.ACCEPTED.value());
        response.put("message", "user updated successfully");
        response.put("data", userResponse);

        return response;
    }

    @Operation(summary = "Update user password", description = "API update password an user to db")
    @PatchMapping("/change-pwd")
    public Map<String, Object> changePassword(@RequestBody @Valid UserPasswordRequest request){
        log.info("Change password for user: {}", request);

        userService.changePassword(request);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.NO_CONTENT.value());
        response.put("message", "Password changed successfully");
        response.put("data", "");

        return response;
    }

    @Operation(summary = "Inactivate user", description = "API inactivate user in database")
    @DeleteMapping("/del/{userId}")
    public Map<String, Object> deleteUser(@PathVariable @Min(value = 1, message = "id must be greater than or equals than 1") Long userId){
        log.info("Deleting user: {}", userId);

        userService.delete(userId);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.RESET_CONTENT.value());
        response.put("message", "User deleted successfully");
        response.put("data", "");

        return response;
    }
}
