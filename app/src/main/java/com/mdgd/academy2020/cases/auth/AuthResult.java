package com.mdgd.academy2020.cases.auth;

public final class AuthResult {
    public final String token;
    public final String avatarUrl;
    public final String avatarPath;

    AuthResult(String token, String avatarUrl, String avatarPath) {
        this.token = token;
        this.avatarUrl = avatarUrl;
        this.avatarPath = avatarPath;
    }
}
