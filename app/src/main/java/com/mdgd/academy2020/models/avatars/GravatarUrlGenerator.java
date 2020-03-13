package com.mdgd.academy2020.models.avatars;

public class GravatarUrlGenerator implements AvatarUrlGenerator {
    @Override
    public String generate(String hash) {
        // https://www.gravatar.com/avatar/5148955656?d=identicon&r=g
        // d: identicon, monsterid, robohash
        // r: g, pg, r, x
        return String.format("https://www.gravatar.com/avatar/%1$s?d=robohash&r=x&s=512", hash);
    }
}
