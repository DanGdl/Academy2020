package com.mdgd.academy2020.models.cache.profile;

public interface ProfileCache {

    String getImageUrl();

    void putImageUrl(String imageUrl);

    String getNickname();

    void putNickname(String nickname);

    String getPassword();

    void putPassword(String password);

    String getEmail();

    void putEmail(String email);
}
