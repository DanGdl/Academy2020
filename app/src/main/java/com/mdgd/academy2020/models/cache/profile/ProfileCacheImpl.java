package com.mdgd.academy2020.models.cache.profile;

import com.mdgd.academy2020.models.repo.user.User;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ProfileCacheImpl implements ProfileCache {
    private String password = "";
    private User user = new User();
    private User originalUser;
    private final PublishSubject<String> avatarUrlObservable = PublishSubject.create();

    public String getImageUrl() {
        return user.getImageUrl();
    }

    public void putImageUrl(String imageUrl) {
        user.setImageUrl(imageUrl);
        avatarUrlObservable.onNext(imageUrl);
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
        if (user == null) {
            this.user = new User();
        } else {
            this.user = user.copy();
            avatarUrlObservable.onNext(user.getImageUrl());
        }
    }

    @Override
    public Observable<String> getImageUrlObservable() {
        return avatarUrlObservable;
    }

    @Override
    public boolean hasOriginalUser() {
        return originalUser != null;
    }

    @Override
    public void putOriginalUser(User data) {
        if (data == null) {
            originalUser = null;
        } else {
            originalUser = data.copy();
        }
    }

    @Override
    public User getOriginalUser() {
        return originalUser.copy();
    }
}
