package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.ModelProvider;

class SignInFragmentLocator {

    SignInContract.Controller createController() {
        final ModelProvider provider = App.getInstance().getModelProvider();
        return new SignInController(provider.getEmailValidator(), provider.getPasswordValidator(),
                new UserAuthUseCase(provider.getNetwork(), provider.getPrefs(), provider.getFiles()), provider.getPrefs(),
                provider.getAvatarUrlGenerator());
    }
}
