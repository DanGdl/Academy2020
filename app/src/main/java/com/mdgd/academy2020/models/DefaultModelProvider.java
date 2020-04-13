package com.mdgd.academy2020.models;

import android.app.Application;

import com.mdgd.academy2020.models.cache.Cache;
import com.mdgd.academy2020.models.cache.CacheImpl;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.cache.profile.ProfileCacheImpl;
import com.mdgd.academy2020.models.files.Files;
import com.mdgd.academy2020.models.files.FilesImpl;
import com.mdgd.academy2020.models.network.FirebaseNetwork;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.prefs.AppPrefs;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.repo.avatar.AvatarRepo;
import com.mdgd.academy2020.models.repo.avatar.AvatarRepository;
import com.mdgd.academy2020.models.repo.avatar.generator.AvatarUrlGenerator;
import com.mdgd.academy2020.models.repo.avatar.generator.GravatarUrlGenerator;
import com.mdgd.academy2020.models.repo.user.UserRepo;
import com.mdgd.academy2020.models.repo.user.UserRepository;
import com.mdgd.academy2020.models.repo.user.dao.UserDao;
import com.mdgd.academy2020.models.repo.user.dao.UserDaoSql;
import com.mdgd.academy2020.models.validators.EmailValidator;
import com.mdgd.academy2020.models.validators.PasswordValidator;
import com.mdgd.academy2020.models.validators.Validator;

import java.lang.ref.WeakReference;

public class DefaultModelProvider implements ModelProvider {
    private final Application app;
    private final Cache cache;
    private final Network network;
    private final Prefs prefs;
    private final ProfileCache profileCache;
    private AvatarRepository avatarRepo;
    private WeakReference<AvatarUrlGenerator> avatarUrlGenerator;

    public DefaultModelProvider(Application app) {
        this.app = app;
        cache = new CacheImpl();
        prefs = new AppPrefs(app);
        network = new FirebaseNetwork(getFiles());
        profileCache = new ProfileCacheImpl();
    }

    @Override
    public void onConfigurationChanged() {
        if (avatarUrlGenerator != null && avatarUrlGenerator.get() != null) {
            avatarUrlGenerator.get().onConfigurationChanged();
        }
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
        final GravatarUrlGenerator gravatarUrlGenerator = new GravatarUrlGenerator(app);
        avatarUrlGenerator = new WeakReference<>(gravatarUrlGenerator);
        return gravatarUrlGenerator;
    }

    @Override
    public Files getFiles() {
        return new FilesImpl(app);
    }

    @Override
    public AvatarRepository getAvatarRepository() {
        if (avatarRepo == null) {
            avatarRepo = new AvatarRepo(getProfileCache(), getAvatarUrlGenerator(), getPrefs(), getFiles(), getNetwork());
        }
        return avatarRepo;
    }

    @Override
    public UserDao getUserDao() {
        return new UserDaoSql(app);
    }

    @Override
    public UserRepository getUserRepository() {
        return new UserRepo(getAvatarRepository(), getNetwork(), getUserDao());
    }

    @Override
    public ProfileCache getProfileCache() {
        return profileCache;
    }
}
