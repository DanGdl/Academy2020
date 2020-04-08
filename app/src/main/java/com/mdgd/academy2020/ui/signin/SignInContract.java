package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.arch.Contract;
import com.mdgd.academy2020.arch.support.auth.AuthContract;
import com.mdgd.academy2020.models.network.Result;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SignInContract {

    public interface Controller extends AuthContract.Controller<View> {

        void execSignIn();

        void setupSubscriptions();

        void takePicture();

        void generateImage();
    }

    public interface View extends AuthContract.View {

        void proceedToLobby();

        Observable<String> getNickNameObservable();

        void setNickNameError(String errorMessage);

        Observable<String> getPasswordVerificationObservable();

        void setSignInEnabled(Boolean isEnabled);

        void setPasswordVerificationError(String errorMsg);

        void loadAvatar(String data);

        Single<Result<String>> showTakePictureScreen();

        void showError(String title, String message);
    }

    public interface Host extends Contract.Host {
        Single<Result<String>> showTakePictureScreen();

        void proceedToLobby();
    }
}
