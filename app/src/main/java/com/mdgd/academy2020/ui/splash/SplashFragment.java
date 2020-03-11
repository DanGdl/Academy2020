package com.mdgd.academy2020.ui.splash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

public class SplashFragment extends MvpFragment<SplashContract.Controller, SplashContract.Host> implements SplashContract.View {
    private SplashContract.Controller controller;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    public SplashFragment() {
        setRetainInstance(true);
        controller = new SplashFragmentLocator().createController();
    }

    @Override
    protected SplashContract.Controller getController() {
        return controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getController().checkUserStatus();
    }

    @Override
    public void proceedToLobby() {
        if (hasCallBack()) {
            getCallBack().proceedToLobby();
        }
    }

    @Override
    public void proceedToAuth() {
        if (hasCallBack()) {
            getCallBack().proceedToAuth();
        }
    }
}
