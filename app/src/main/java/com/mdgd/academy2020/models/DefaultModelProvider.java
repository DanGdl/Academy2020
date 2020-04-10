package com.mdgd.academy2020.models;

import android.app.Application;

import com.mdgd.academy2020.models.cache.Cache;
import com.mdgd.academy2020.models.cache.CacheImpl;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.cache.profile.ProfileCacheImpl;
import com.mdgd.academy2020.models.dao.Dao;
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
import com.mdgd.academy2020.models.repo.user.User;
import com.mdgd.academy2020.models.repo.user.UserDaoSql;
import com.mdgd.academy2020.models.repo.user.UserRepo;
import com.mdgd.academy2020.models.repo.user.UserRepository;
import com.mdgd.academy2020.models.validators.EmailValidator;
import com.mdgd.academy2020.models.validators.PasswordValidator;
import com.mdgd.academy2020.models.validators.Validator;

public class DefaultModelProvider implements ModelProvider {
    private final Application app;
    private final Cache cache;
    private final Network network;
    private final Prefs prefs;
    private final ProfileCache profileCache;
    private AvatarRepository avatarRepo;

    public DefaultModelProvider(Application app) {
        this.app = app;
        cache = new CacheImpl();
        prefs = new AppPrefs(app);
        network = new FirebaseNetwork(getFiles());
        profileCache = new ProfileCacheImpl();
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
    public Dao<User> getUserDao() {
        return new UserDaoSql();
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
