package com.mdgd.academy2020.cases.auth;

public final class AuthParams {
    static final int TYPE_SIGN_IN = 1;
    static final int TYPE_LOG_IN = 2;

    private final String nickname;
    private final String email;
    private final String password;
    private final String imageUrl;
    private final int type;

    public static AuthParams newSignInParams(String nickname, String email, String password, String imageUrl) {
        return new AuthParams(nickname, email, password, imageUrl, TYPE_SIGN_IN);
    }

    public static AuthParams newLogInParams(String email, String password) {
        return new AuthParams("", email, password, "", TYPE_LOG_IN);
    }

    private AuthParams(String nickname, String email, String password, String imageUrl, int type) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.type = type;
    }


    int getType() {
        return type;
    }

    String getNickName() {
        return nickname;
    }

    String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    String getImageUrl() {
        return imageUrl;
    }
}
