package com.littlepig.controller.response;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String gender;
    private Date birthday;
    private String email;
    private String phone;
}
