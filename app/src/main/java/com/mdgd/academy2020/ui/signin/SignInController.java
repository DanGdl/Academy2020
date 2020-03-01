package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.util.TextUtil;

class SignInController extends MvpController<SignInContract.View> implements SignInContract.Controller {

    private final Network network;

    SignInController(Network network) {
        this.network = network;
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
            view.showProgress();
            network.createNewUser("", "", "", "", result -> {
                if (result.isFail()) {

                } else {

                }
            });
        }
    }
}
