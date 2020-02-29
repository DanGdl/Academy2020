package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.arch.Contract;

public class LoginContract {

    public interface Controller extends Contract.Controller<View> {
        String validateEmail(String email);

        String validatePassword(String password);

        void execLogIn(String email, String password);
    }

    public interface View extends Contract.View {
        void proceedToLobby();

        void showError(String title, String message);
    }

    public interface Host extends Contract.Host {
        void proceedToLobby();

        void showError(String title, String message);
    }
}
