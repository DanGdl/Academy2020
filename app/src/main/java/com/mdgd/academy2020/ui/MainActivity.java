package com.mdgd.academy2020.ui;

import android.os.Bundle;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.HostActivity;
import com.mdgd.academy2020.arch.progress.ProgressContainer;
import com.mdgd.academy2020.ui.progress.ProgressDialogWrapper;
import com.mdgd.academy2020.ui.splash.SplashFragment;

public class MainActivity extends HostActivity implements ProgressContainer {
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
}
