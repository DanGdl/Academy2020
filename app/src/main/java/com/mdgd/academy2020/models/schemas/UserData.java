package com.mdgd.academy2020.models.schemas;

public class UserData {
    private String email;
    private String nickname;
    private String imageUrl;

    public UserData(String email, String nickName, String imageUrl) {
        this.email = email;
        this.nickname = nickName;
        this.imageUrl = imageUrl;
    }

    public UserData(String email) {
        this.email = email;
        this.nickname = "";
        this.imageUrl = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
