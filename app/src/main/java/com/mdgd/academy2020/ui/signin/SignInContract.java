package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.arch.Contract;
import com.mdgd.academy2020.models.network.Result;

import io.reactivex.Single;

public class SignInContract {

    public interface Controller extends Contract.Controller<View> {
        String validatePassword(String password);

        String checkPasswordVerification(String password, String passwordVerification);

        void execSignIn();
    }

    public interface View extends Contract.View {
        void showError(String title, String message);

        void proceedToLobby();
    }

    public interface Host extends Contract.Host {
        Single<Result<String>> showTakePictureScreen();

        void showError(String title, String message);

        void proceedToLobby();
    }
}
