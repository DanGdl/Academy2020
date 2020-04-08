package com.mdgd.academy2020.arch.support.auth;

import com.mdgd.academy2020.arch.Contract;

import io.reactivex.Observable;

public class AuthContract {

    public interface Controller<T extends View> extends Contract.Controller<T> {
    }

    public interface View extends Contract.View {
        Observable<String> getEmailObservable();

        void setEmailError(String errorMsg);

        Observable<String> getPasswordObservable();

        void setPasswordError(String errorMsg);

        void showError(String title, String message);

        void proceedToLobby();
    }
}
