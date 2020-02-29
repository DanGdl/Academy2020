package com.mdgd.academy2020.ui.splash;

import com.mdgd.academy2020.arch.Contract;

public class SplashContract {

    public interface Controller extends Contract.Controller<View> {
        void checkUserStatus();
    }

    public interface View extends Contract.View {
        void proceedToLobby();

        void proceedToAuth();
    }

    public interface Host extends Contract.Host {
        void proceedToLobby();

        void proceedToAuth();
    }
}
