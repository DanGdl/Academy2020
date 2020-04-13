package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.cases.LogoutUseCase;
import com.mdgd.academy2020.models.ModelProvider;

class SignInFragmentLocator {

    SignInContract.Controller createController(int mode) {
        final ModelProvider provider = App.getInstance().getModelProvider();

        return new SignInController(provider.getNetwork(), provider.getPrefs(), provider.getProfileCache(),
                provider.getEmailValidator(), provider.getPasswordValidator(), provider.getUserRepository(),
                provider.getAvatarRepository(), new LogoutUseCase(provider.getNetwork(), provider.getPrefs(), provider.getUserRepository()));
    }
}
