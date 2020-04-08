package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.ModelProvider;
import com.mdgd.academy2020.models.cache.profile.ProfileCacheImpl;

class LoginFragmentLocator {

    LoginContract.Controller createController() {
        final ModelProvider provider = App.getInstance().getModelProvider();
        final ProfileCacheImpl profileCache = new ProfileCacheImpl();
        return new LoginController(provider.getEmailValidator(), provider.getPasswordValidator(),
                new UserAuthUseCase(provider.getNetwork(), provider.getPrefs(), provider.getAvatarRepository(profileCache)),
                profileCache);
    }
}
