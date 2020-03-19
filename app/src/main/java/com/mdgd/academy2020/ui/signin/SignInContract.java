package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.arch.Contract;
import com.mdgd.academy2020.cases.auth.AuthView;
import com.mdgd.academy2020.models.network.Result;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SignInContract {

    public interface Controller extends Contract.Controller<View> {

        void execSignIn();

        void setupSubscriptions();

        void takePicture();

        void generateImage();
    }

    public interface View extends Contract.View, AuthView {
        void showError(String title, String message);

        void proceedToLobby();

        Observable<String> getNickNameObservable();

        void setNickNameError(String errorMessage);

        Observable<String> getPasswordObservable();

        void setPasswordError(String errorMsg);

        Observable<String> getPasswordVerificationObservable();

        void setSignInEnabled(Boolean isEnabled);

        void setPasswordVerificationError(String errorMsg);

        Single<Result<String>> showTakePictureScreen();

        void loadAvatar(String data);

        Observable<String> getEmailObservable();

        void setEmailError(String errorMessage);
    }

    public interface Host extends Contract.Host {
        Single<Result<String>> showTakePictureScreen();

        void showError(String title, String message);

        void proceedToLobby();
    }
}
