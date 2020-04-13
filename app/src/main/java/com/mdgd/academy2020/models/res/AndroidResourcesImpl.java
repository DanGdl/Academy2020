package com.mdgd.academy2020.models.res;

import android.content.Context;

public class AndroidResourcesImpl implements AndroidResources {
    private final Context ctx;

    public AndroidResourcesImpl(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public String getString(int strResId, Object... args) {
        return ctx.getString(strResId, args);
    }
}
