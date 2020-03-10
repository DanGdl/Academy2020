package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.models.ModelProvider;
import com.mdgd.academy2020.models.cases.auth.UserAuthUseCase;

class SignInFragmentLocator {

    SignInContract.Controller createController() {
        final ModelProvider provider = App.getInstance().getModelProvider();
        return new SignInController(provider.getEmailValidator(), provider.getPasswordValidator(),
                new UserAuthUseCase(provider.getNetwork(), provider.getPrefs()));
    }
}
