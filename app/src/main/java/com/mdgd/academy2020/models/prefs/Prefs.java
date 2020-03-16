package com.mdgd.academy2020.models.prefs;

public interface Prefs {
    String getAuthToken();

    void putAuthToken(String token);

    String getAvatarHash();

    void putImageHash(String hash);

    void putAvatarUrl(String avatarUrl);

    String getAvatarUrl();

    void putAvatarPath(String avatarPath);

    String getAvatarPath();
}
