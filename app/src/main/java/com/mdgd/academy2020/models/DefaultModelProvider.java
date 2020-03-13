package com.mdgd.academy2020.models;

import android.app.Application;

import com.mdgd.academy2020.models.avatars.AvatarUrlGenerator;
import com.mdgd.academy2020.models.avatars.GravatarUrlGenerator;
import com.mdgd.academy2020.models.cache.Cache;
import com.mdgd.academy2020.models.cache.CacheImpl;
import com.mdgd.academy2020.models.network.FirebaseNetwork;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.prefs.AppPrefs;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.validators.EmailValidator;
import com.mdgd.academy2020.models.validators.PasswordValidator;
import com.mdgd.academy2020.models.validators.Validator;

public class DefaultModelProvider implements ModelProvider {
    private final Application app;
    private final Cache cache;
    private final Network network;
    private final Prefs prefs;

    public DefaultModelProvider(Application app) {
        this.app = app;
        cache = new CacheImpl();
        prefs = new AppPrefs(app);
        network = new FirebaseNetwork(app);
    }

    @Override
    public Cache getCache() {
        return cache;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Prefs getPrefs() {
        return prefs;
    }

    @Override
    public Validator<String> getEmailValidator() {
        return new EmailValidator(app);
    }

    @Override
    public Validator<String> getPasswordValidator() {
        return new PasswordValidator(app);
    }

    @Override
    public AvatarUrlGenerator getAvatarUrlGenerator() {
        return new GravatarUrlGenerator();
    }
}
