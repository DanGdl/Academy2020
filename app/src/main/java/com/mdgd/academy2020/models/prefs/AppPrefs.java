package com.mdgd.academy2020.models.prefs;

import android.content.Context;

public class AppPrefs extends BasicPrefsImpl {

    public AppPrefs(Context ctx) {
        super(ctx);
    }

    @Override
    public String getDefaultPrefsFileName() {
        return "lol_this_is_prefs";
    }
}
