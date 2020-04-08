package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.arch.Contract;
import com.mdgd.academy2020.arch.support.auth.AuthContract;

public class LoginContract {

    public interface Controller extends Contract.Controller<View> {

        void execLogIn();

        void setupSubscriptions();
    }

    public interface View extends Contract.View, AuthContract.View {
        void proceedToLobby();

        void showError(String title, String message);

        void setLoginEnabled(Boolean isEnabled);
    }

    public interface Host extends Contract.Host {
        void proceedToLobby();
    }
}
