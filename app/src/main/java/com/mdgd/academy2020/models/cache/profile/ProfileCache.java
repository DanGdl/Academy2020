package com.mdgd.academy2020.models.cache.profile;

import com.mdgd.academy2020.models.repo.user.User;

import io.reactivex.Observable;

public interface ProfileCache {

    String getImageUrl();

    void putImageUrl(String imageUrl);

    String getNickname();

    void putNickname(String nickname);

    String getPassword();

    void putPassword(String password);

    String getEmail();

    void putEmail(String email);

    String getAvatarType();

    void putAvatarType(String avatarType);

    String getAvatarHash();

    void putAvatarHash(String avatarHash);

    User getUser();

    void putUser(User user);

    Observable<String> getImageUrlObservable();

    boolean hasOriginalUser();

    void putOriginalUser(User data);

    User getOriginalUser();
}
