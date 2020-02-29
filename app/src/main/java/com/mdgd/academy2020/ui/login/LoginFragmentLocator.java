package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.models.ModelProvider;

class LoginFragmentLocator {

    LoginContract.Controller createController() {
        final ModelProvider provider = App.getInstance().getModelProvider();
        return new LoginController(provider.getNetwork());
    }
}
