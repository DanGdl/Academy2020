package com.mdgd.academy2020.models.prefs;

import android.content.Context;

public class AppPrefs extends BasicPrefsImpl implements Prefs {

    private static final String AUTH_TOKEN = "auth_token";
    private static final String AVATAR_HASH = "avatar_hash";

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
    public String getImageHash() {
        return get(AVATAR_HASH, "");
    }

    @Override
    public void putImageHash(String hash) {
        put(AVATAR_HASH, hash);
    }
}
