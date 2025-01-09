package com.littlepig.controller.request;

import com.littlepig.common.enums.Gender;
import com.littlepig.common.enums.UserType;
import com.littlepig.model.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@ToString
public class UserCreationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String userName;
    private Gender gender;
    private Date birthday;
    private String email;
    private String phone;
    private UserType type;
    private List<AddressRequest> addresses; //home, office
}
