package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.arch.Contract;
import com.mdgd.academy2020.models.cases.auth.AuthView;

import io.reactivex.Observable;

public class LoginContract {

    public interface Controller extends Contract.Controller<View> {

        void execLogIn();

        void setupSubscriptions();
    }

    public interface View extends Contract.View, AuthView {
        void proceedToLobby();

        void showError(String title, String message);

        Observable<String> getEmailObservable();

        void setEmailError(String errorMsg);

        Observable<String> getPasswordObservable();

        void setPasswordError(String errorMsg);

        void setLoginEnabled(Boolean isEnabled);
    }

    public interface Host extends Contract.Host {
        void proceedToLobby();

        void showError(String title, String message);
    }
}
