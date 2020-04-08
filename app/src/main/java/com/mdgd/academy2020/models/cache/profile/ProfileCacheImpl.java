package com.mdgd.academy2020.models.cache.profile;

public class ProfileCacheImpl implements ProfileCache {
    private String imageUrl = "";
    private String nickname = "";
    private String password = "";
    private String email = "";

    public String getImageUrl() {
        return imageUrl;
    }

    public void putImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void putNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void putPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void putEmail(String email) {
        this.email = email;
    }
}
