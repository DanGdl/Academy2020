package com.mdgd.academy2020.models.cache.profile;

import com.mdgd.academy2020.models.repo.user.User;

public class ProfileCacheImpl implements ProfileCache {
    private String password = "";
    private User user = new User();

    public String getImageUrl() {
        return user.getImageUrl();
    }

    public void putImageUrl(String imageUrl) {
        user.setImageUrl(imageUrl);
    }

    public String getNickname() {
        return user.getNickname();
    }

    public void putNickname(String nickname) {
        user.setNickname(nickname);
    }

    public String getPassword() {
        return password;
    }

    public void putPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void putEmail(String email) {
        user.setEmail(email);
    }

    public String getAvatarType() {
        return user.getAvatarType();
    }

    public void putAvatarType(String avatarType) {
        user.setAvatarType(avatarType);
    }

    public String getAvatarHash() {
        return user.getAvatarHash();
    }

    public void putAvatarHash(String avatarHash) {
        user.setAvatarHash(avatarHash);
    }

    @Override
    public User getUser() {
        return user.copy();
    }

    @Override
    public void putUser(User user) {
        this.user = user.copy();
    }
}
