package com.mdgd.academy2020.ui.profile;

import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.prefs.Prefs;

class ProfileController extends MvpController<ProfileContract.View> implements ProfileContract.Controller {

    private final Network network;
    private final Prefs prefs;

    public ProfileController(Network network, Prefs prefs) {
        this.network = network;
        this.prefs = prefs;
    }


    @Override
    public void setupSubscriptions() {
        onStopDisposable.add(view.getLogoutObservable()
                .subscribe(event -> {
                    network.logOut();
                    prefs.clear();
                }));
    }
}
