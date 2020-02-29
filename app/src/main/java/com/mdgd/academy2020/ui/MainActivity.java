package com.mdgd.academy2020.ui;

import android.os.Bundle;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.HostActivity;
import com.mdgd.academy2020.arch.progress.ProgressContainer;
import com.mdgd.academy2020.ui.auth.AuthContract;
import com.mdgd.academy2020.ui.auth.AuthFragment;
import com.mdgd.academy2020.ui.error.ErrorFragment;
import com.mdgd.academy2020.ui.lobby.LobbyFragment;
import com.mdgd.academy2020.ui.login.LoginContract;
import com.mdgd.academy2020.ui.login.LoginFragment;
import com.mdgd.academy2020.ui.progress.ProgressDialogWrapper;
import com.mdgd.academy2020.ui.signin.SignInContract;
import com.mdgd.academy2020.ui.signin.SignInFragment;
import com.mdgd.academy2020.ui.splash.SplashContract;
import com.mdgd.academy2020.ui.splash.SplashFragment;

public class MainActivity extends HostActivity implements ProgressContainer, SplashContract.Host,
        AuthContract.Host, LoginContract.Host, SignInContract.Host {
    private ProgressDialogWrapper progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragment(SplashFragment.newInstance());
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    public boolean hasProgress() {
        return true;
    }

    @Override
    public void showProgress(String title, String message) {
        if (progress == null) {
            progress = new ProgressDialogWrapper(this, title, message);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void showProgress(int titleRes, int messageRes) {
        if (progress == null) {
            progress = new ProgressDialogWrapper(this, titleRes, messageRes);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialogWrapper(this);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    public void proceedToLobby() {
        replaceFragment(LobbyFragment.newInstance());
    }

    @Override
    public void showError(String title, String message) {
        ErrorFragment.newInstance(title, message).show(getSupportFragmentManager(), "error");
    }

    @Override
    public void proceedToAuth() {
        replaceFragment(AuthFragment.newInstance());
    }

    @Override
    public void proceedToLogIn() {
        addFragmentToBackStack(LoginFragment.newInstance());
    }

    @Override
    public void proceedToSignIn() {
        addFragmentToBackStack(SignInFragment.newInstance());
    }
}
