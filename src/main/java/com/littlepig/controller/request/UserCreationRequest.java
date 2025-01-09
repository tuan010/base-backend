package com.littlepig.controller.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserCreationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String userName;
    private String gender;
    private Date birthday;
    private String email;
    private String phone;
}
