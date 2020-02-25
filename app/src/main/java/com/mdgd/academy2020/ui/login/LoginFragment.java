package com.mdgd.academy2020.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

public class LoginFragment extends MvpFragment<LoginContract.Controller, LoginContract.Host> implements LoginContract.View {
    private LoginContract.Controller controller;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        controller = new LoginFragmentLocator().createController();
    }

    @Override
    protected LoginContract.Controller getController() {
        return controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
