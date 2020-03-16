package com.mdgd.academy2020.models.prefs;

import android.content.Context;

public class AppPrefs extends BasicPrefsImpl implements Prefs {

    private static final String AUTH_TOKEN = "auth_token";
    private static final String AVATAR_HASH = "avatar_hash";
    private static final String AVATAR_PATH = "avatar_path";
    private static final String AVATAR_URL = "avatar_url";

    public AppPrefs(Context ctx) {
        super(ctx);
    }

    @Override
    public String getDefaultPrefsFileName() {
        return "lol_this_is_prefs";
    }

    @Override
    public String getAuthToken() {
        return get(AUTH_TOKEN, "");
    }

    @Override
    public void putAuthToken(String token) {
        put(AUTH_TOKEN, token);
    }

    @Override
    public String getAvatarHash() {
        return get(AVATAR_HASH, "");
    }

    @Override
    public void putImageHash(String hash) {
        put(AVATAR_HASH, hash);
    }

    @Override
    public void putAvatarUrl(String avatar) {
        put(AVATAR_URL, avatar);
    }

    @Override
    public String getAvatarUrl() {
        return get(AVATAR_URL, "");
    }

    @Override
    public void putAvatarPath(String avatarPath) {
        put(AVATAR_PATH, avatarPath);
    }

    @Override
    public String getAvatarPath() {
        return get(AVATAR_PATH, "");
    }
}
