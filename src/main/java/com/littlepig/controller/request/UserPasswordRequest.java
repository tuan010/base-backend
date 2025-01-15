package com.littlepig.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UserPasswordRequest implements Serializable {
    @NotNull(message = "id must be not null")
    @Min(value = 1, message = "id must be greater than or equals than 1")
    private Long id;
    @NotBlank(message = "password must be not blank")
    private String password;
    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;
}
