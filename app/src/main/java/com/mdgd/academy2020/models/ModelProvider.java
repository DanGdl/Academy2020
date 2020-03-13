package com.mdgd.academy2020.models;

import com.mdgd.academy2020.models.avatars.AvatarUrlGenerator;
import com.mdgd.academy2020.models.cache.Cache;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.validators.Validator;

public interface ModelProvider {

    Cache getCache();

    Network getNetwork();

    Prefs getPrefs();

    Validator<String> getEmailValidator();

    Validator<String> getPasswordValidator();

    AvatarUrlGenerator getAvatarUrlGenerator();
}
