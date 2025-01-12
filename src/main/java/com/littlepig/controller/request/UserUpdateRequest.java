package com.littlepig.controller.request;

import com.littlepig.common.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserUpdateRequest implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private Gender gender;
    private Date birthday;
    private String email;
    private String phone;
    private List<AddressRequest> addresses;
}
