package com.mdgd.academy2020.models.schemas;

public class UserData {
    private String email;
    private String nickname;
    private String imageUrl;
    private String avatarHash;
    private String avatarType;

    public UserData(String email, String nickName, String imageUrl, String avatarHash, String avatarType) {
        this.email = email;
        this.nickname = nickName;
        this.imageUrl = imageUrl;
        this.avatarHash = avatarHash;
        this.avatarType = avatarType;
    }

    public UserData(String email) {
        this.email = email;
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

    public String getAvatarHash() {
        return avatarHash;
    }

    public void setAvatarHash(String avatarHash) {
        this.avatarHash = avatarHash;
    }

    public String getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(String avatarType) {
        this.avatarType = avatarType;
    }
}
