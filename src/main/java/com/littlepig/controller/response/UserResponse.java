package com.littlepig.controller.response;

import com.littlepig.common.enums.Gender;
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
    private Gender gender;
    private Date birthday;
    private String email;
    private String phone;
}
