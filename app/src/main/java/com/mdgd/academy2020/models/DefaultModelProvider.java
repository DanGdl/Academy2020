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
import com.mdgd.academy2020.models.repo.user.UserRepo;
import com.mdgd.academy2020.models.repo.user.UserRepository;
import com.mdgd.academy2020.models.repo.user.dao.UserDao;
import com.mdgd.academy2020.models.repo.user.dao.UserDaoSql;
import com.mdgd.academy2020.models.res.AndroidResources;
import com.mdgd.academy2020.models.res.AndroidResourcesImpl;
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
    private final AndroidResources androidResources;

    public DefaultModelProvider(Application app) {
        this.app = app;
        cache = new CacheImpl();
        prefs = new AppPrefs(app);
        network = new FirebaseNetwork(getFiles());
        profileCache = new ProfileCacheImpl();
        androidResources = new AndroidResourcesImpl(app);
    }

    @Override
    public void onConfigurationChanged() {
        if (avatarRepo != null) {
            avatarRepo.onConfigurationChanged();
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
    public Files getFiles() {
        return new FilesImpl(app);
    }

    @Override
    public AvatarRepository getAvatarRepository() {
        if (avatarRepo == null) {
            avatarRepo = new AvatarRepo(getProfileCache(), getPrefs(), getFiles(), getNetwork(), getAndroidResources());
        }
        return avatarRepo;
    }

    @Override
    public AndroidResources getAndroidResources() {
        return androidResources;
    }

    @Override
    public UserDao getUserDao() {
        return new UserDaoSql(app);
    }

    @Override
    public UserRepository getUserRepository() {
        return new UserRepo(getAvatarRepository(), getNetwork(), getUserDao(), getProfileCache());
    }

    @Override
    public ProfileCache getProfileCache() {
        return profileCache;
    }
}
