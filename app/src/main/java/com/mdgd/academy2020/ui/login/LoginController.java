package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.network.Network;

class LoginController extends MvpController<LoginContract.View> implements LoginContract.Controller {
    private final Network network;

    LoginController(Network network) {
        this.network = network;
    }

    @Override
    public String validateEmail(String email) {
        return null;
    }

    @Override
    public String validatePassword(String password) {
        return null;
    }

    @Override
    public void execLogIn(String email, String password) {
        onStopDisposable.add(network.execLogin(email, password)
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
                .doOnSuccess(result -> {
                    if (hasView()) {
                        view.hideProgress();
                    }
                    if (result.isFail()) {
                        if (hasView()) {
                            view.showError(view.getString(R.string.login_failed), result.error.getMessage());
                        }
                    } else {
                        if (hasView()) {
                            // todo make additional stuff?
                            view.showToast(R.string.login_successful);
                            view.proceedToLobby();
                        }
                    }
                }).subscribe());
    }
}
