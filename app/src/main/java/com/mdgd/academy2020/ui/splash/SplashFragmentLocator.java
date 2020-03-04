package com.mdgd.academy2020.ui.splash;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.models.ModelProvider;

class SplashFragmentLocator {

    SplashContract.Controller createController() {
        final ModelProvider provider = App.getInstance().getModelProvider();
        return new SplashController(provider.getPrefs());
    }
}
