package com.littlepig.controller.request;

import com.littlepig.common.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserUpdateRequest implements Serializable {

    @NotNull(message = "id must be not null")
    @Min(value = 1, message = "id must be greater than or equals than 1")
    private Long id;
    @NotBlank(message = "firstName must be not blank")
    private String firstName;
    @NotBlank(message = "lastName must be not blank")
    private String lastName;
    private String userName;
    private Gender gender;
    private Date birthday;
    @Email(message = "Email is invalid")
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
}
