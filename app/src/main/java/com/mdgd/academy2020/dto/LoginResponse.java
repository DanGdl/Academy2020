package com.mdgd.academy2020.dto;

public class LoginResponse {
    public final String token;
    public final String avatarUrl;
    public final String avatarPath;

    public LoginResponse(String token, String avatarUrl, String avatarPath) {
        this.token = token;
        this.avatarUrl = avatarUrl;
        this.avatarPath = avatarPath;
    }
}
