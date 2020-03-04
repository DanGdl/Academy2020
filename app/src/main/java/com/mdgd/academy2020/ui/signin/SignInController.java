package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.util.TextUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

class SignInController extends MvpController<SignInContract.View> implements SignInContract.Controller {

    private final Network network;
    private final Prefs prefs;

    SignInController(Network network, Prefs prefs) {
        this.network = network;
        this.prefs = prefs;
    }

    @Override
    public String validatePassword(String password) {
        return !TextUtil.isEmpty(password) && password.length() > 6 && hasView() ? null : view.getString(R.string.password_too_short);
    }

    @Override
    public String checkPasswordVerification(String password, String passwordVerification) {
        return !TextUtil.isEmpty(password) && password.equals(passwordVerification) && hasView() ? null : view.getString(R.string.verification_is_not_same);
    }

    @Override
    public void execSignIn() {
        if (hasView()) {
            onStopDisposable.add(network.createNewUser("", "", "", "")
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        if (hasView()) {
                            view.showProgress();
                        }
                    })
                    .doOnError(throwable -> {
                        if (hasView()) {
                            view.hideProgress();
                            view.showError(view.getString(R.string.unknown_error), throwable.getMessage());
                        }
                    })
                    .subscribe(result -> {
                        if (result.isFail()) {
                            view.showError(view.getString(R.string.login_failed), result.error.getMessage());
                        } else {
                            // todo make additional stuff?
                            prefs.putAuthToken(result.data);
                            view.showToast(R.string.login_successful);
                            view.proceedToLobby();
                        }
                    })
            );
        }
    }
}
