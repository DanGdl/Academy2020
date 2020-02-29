package com.mdgd.academy2020.ui.splash;

import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.network.Network;

class SplashController extends MvpController<SplashContract.View> implements SplashContract.Controller {
    private final Network network;

    public SplashController(Network network) {
        this.network = network;
    }

    @Override
    public void checkUserStatus() {
        if (network.hasUser()) {
            if (hasView()) {
                view.proceedToLobby();
            }
        } else {
            if (hasView()) {
                view.proceedToAuth();
            }
        }
    }
}
