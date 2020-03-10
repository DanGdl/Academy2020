package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.models.ModelProvider;
import com.mdgd.academy2020.models.cases.auth.UserAuthUseCase;

class LoginFragmentLocator {

    LoginContract.Controller createController() {
        final ModelProvider provider = App.getInstance().getModelProvider();
        return new LoginController(provider.getEmailValidator(), provider.getPasswordValidator(),
                new UserAuthUseCase(provider.getNetwork(), provider.getPrefs()));
    }
}
