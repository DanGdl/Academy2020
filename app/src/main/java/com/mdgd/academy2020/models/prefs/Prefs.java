package com.mdgd.academy2020.models.prefs;

public interface Prefs {
    String getAuthToken();

    void putAuthToken(String token);

    String getAvatarHash();

    void putImageHash(String hash);

    void clear();

    String getAvatarType();

    void putAvatarType(String avatarType);
}
