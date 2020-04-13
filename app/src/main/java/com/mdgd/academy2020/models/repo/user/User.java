package com.mdgd.academy2020.models.repo.user;

import com.mdgd.academy2020.models.dao.Entity;

public class User extends Entity {
    private String email = "";
    private String nickname = "";
    private String imageUrl = "";
    private String imagePath = "";
    private String uid = "";
    private String avatarHash = "";
    private String avatarType;

    public User(String email, String nickname, String imageUrl, String imagePath, String uid, String avatarHash, String avatarType) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.imagePath = imagePath;
        this.uid = uid;
        this.avatarHash = avatarHash;
        this.avatarType = avatarType;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getUid() {
        return uid;
    }

    public String getAvatarHash() {
        return avatarHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAvatarHash(String avatarHash) {
        this.avatarHash = avatarHash;
    }

    public void setAvatarType(String avatarType) {
        this.avatarType = avatarType;
    }

    public String getAvatarType() {
        return avatarType;
    }

    public User copy() {
        return new User(getEmail(), getNickname(), getImageUrl(), getImagePath(), getUid(), getAvatarHash(), getAvatarType());
    }
}
