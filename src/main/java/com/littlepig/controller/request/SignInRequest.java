package com.littlepig.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {
    private String email;
    private String username;
    private String password;
    private String platform; //web, mobile, miniApp
    private String deviceToken;
    private String versionApp;
}
