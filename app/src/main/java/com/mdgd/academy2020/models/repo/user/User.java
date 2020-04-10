package com.mdgd.academy2020.models.repo.user;

import com.mdgd.academy2020.models.dao.Entity;

public class User extends Entity {
    private final String email;
    private final String nickname;
    private final String imageUrl;
    private final String imagePath;

    public User(String email, String nickname, String imageUrl, String imagePath) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.imagePath = imagePath;
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
}
