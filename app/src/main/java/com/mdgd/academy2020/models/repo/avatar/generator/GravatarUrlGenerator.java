package com.mdgd.academy2020.models.repo.avatar.generator;

import android.content.Context;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.util.TextUtil;

import java.util.HashMap;
import java.util.Map;

public class GravatarUrlGenerator implements AvatarUrlGenerator {
    private final Map<String, String> types = new HashMap<>();
    private final Context ctx;
    private String type = "robohash";

    public GravatarUrlGenerator(Context ctx) {
        this.ctx = ctx;
        types.put(ctx.getString(R.string.avatar_type_abstract), "identicon");
        types.put(ctx.getString(R.string.avatar_type_monster), "monsterid");
        types.put(ctx.getString(R.string.avatar_type_robot), "robohash");
    }

    @Override
    public String generate(String hash) {
        // https://www.gravatar.com/avatar/5148955656?d=identicon&r=g
        // d: identicon, monsterid, robohash
        // r: g, pg, r, x
        return String.format("https://www.gravatar.com/avatar/%1$s?d=robohash&r=x&s=512", hash);
    }

    @Override
    public void setType(String type) {
        if (TextUtil.isEmpty(type)) {
            return;
        }
        this.type = types.get(type);
        if (TextUtil.isEmpty(this.type)) {
            this.type = "robohash";
        }
    }

    @Override
    public void onConfigurationChanged() {
        types.clear();
        types.put(ctx.getString(R.string.avatar_type_abstract), "identicon");
        types.put(ctx.getString(R.string.avatar_type_monster), "monsterid");
        types.put(ctx.getString(R.string.avatar_type_robot), "robohash");
    }
}
