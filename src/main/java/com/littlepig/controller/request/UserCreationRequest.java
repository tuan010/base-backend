package com.littlepig.controller.request;

import com.littlepig.common.enums.Gender;
import com.littlepig.common.enums.UserType;
import com.littlepig.model.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@ToString
public class UserCreationRequest implements Serializable {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;
    @NotBlank(message = "lastName must be not blank")
    private String password;
    private String lastName;
    private String userName;
    private Gender gender;
    private Date birthday;
    @Email(message = "Email is invalid")
    private String email;
    private String phone;
    private UserType type;
    private List<AddressRequest> addresses; //home, office
}
