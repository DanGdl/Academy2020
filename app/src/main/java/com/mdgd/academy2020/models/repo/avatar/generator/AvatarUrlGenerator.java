package com.mdgd.academy2020.models.repo.avatar.generator;

public interface AvatarUrlGenerator {
    String generate(String hash);

    void setType(String type);

    void onConfigurationChanged();
}
