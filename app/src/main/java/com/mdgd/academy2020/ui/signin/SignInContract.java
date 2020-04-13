package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.arch.Contract;
import com.mdgd.academy2020.arch.support.auth.AuthContract;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.repo.user.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SignInContract {

    public static final int MODE_SIGN_IN = 1;
    public static final int MODE_PROFILE = 2;

    public interface Controller extends AuthContract.Controller<View> {

        void execSignIn();

        void setupSubscriptions();

        void takePicture();

        void generateImage();

        void logout();

        void setAvatarType(String type);
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

        void setUser(User user);

        void setAvatarTypes(List<String> types);
    }

    public interface Host extends Contract.Host {
        Single<Result<String>> showTakePictureScreen();

        void proceedToLobby();
    }
}
