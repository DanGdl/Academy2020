package com.mdgd.academy2020.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

public class AuthFragment extends MvpFragment<AuthContract.Controller, AuthContract.Host> implements AuthContract.View, View.OnClickListener {
    private AuthContract.Controller controller;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    public AuthFragment() {
    }

    @Override
    protected AuthContract.Controller getController() {
        return controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.login_btn).setOnClickListener(this);
        view.findViewById(R.id.sign_in_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (hasCallBack()) {
            final int id = v.getId();
            if (R.id.login_btn == id) {
                getCallBack().proceedToLogIn();
            } else if (R.id.sign_in_btn == id) {
                getCallBack().proceedToSignIn();
            }
        }
    }
}
