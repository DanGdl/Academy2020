package com.mdgd.academy2020.ui.profile;

import com.mdgd.academy2020.App;
import com.mdgd.academy2020.models.ModelProvider;

class ProfileFragmentLocator {

    ProfileContract.Controller createController() {
        final ModelProvider provider = App.getInstance().getModelProvider();
        return new ProfileController(provider.getNetwork(), provider.getPrefs());
    }
}
