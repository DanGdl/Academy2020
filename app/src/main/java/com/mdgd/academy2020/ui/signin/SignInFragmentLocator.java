package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.ModelProvider;
import com.mdgd.academy2020.models.avatar.AvatarRepository;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.cache.profile.ProfileCacheImpl;

class SignInFragmentLocator {

    SignInContract.Controller createController(int mode) {
        final ModelProvider provider = App.getInstance().getModelProvider();
        final ProfileCache profileCache = new ProfileCacheImpl();
        final AvatarRepository avatarRepository = provider.getAvatarRepository(profileCache);

        return new SignInController(provider.getNetwork(), provider.getPrefs(), profileCache, provider.getEmailValidator(),
                provider.getPasswordValidator(), new UserAuthUseCase(provider.getNetwork(), provider.getPrefs(), avatarRepository),
                avatarRepository);
    }
}
