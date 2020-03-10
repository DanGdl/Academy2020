package com.mdgd.academy2020.models.cases.auth;

public class AuthParams {
    static final int TYPE_SIGN_IN = 1;
    static final int TYPE_LOG_IN = 2;

    private final String nickname;
    private final String email;
    private final String password;
    private final String imageUrl;
    private final AuthView view;
    private final int type;

    public static AuthParams newSignInParams(String nickname, String email, String password, String imageUrl, AuthView view) {
        return new AuthParams(nickname, email, password, imageUrl, view, TYPE_SIGN_IN);
    }

    public static AuthParams newLogInParams(String email, String password, AuthView view) {
        return new AuthParams("", email, password, "", view, TYPE_SIGN_IN);
    }

    private AuthParams(String nickname, String email, String password, String imageUrl, AuthView view, int type) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.view = view;
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

    boolean hasView() {
        return view != null && view.isStarted();
    }

    public AuthView getView() {
        return view;
    }
}
