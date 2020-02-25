package com.mdgd.academy2020.ui.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

public class SignInFragment extends MvpFragment<SignInContract.Controller, SignInContract.Host> implements SignInContract.View {
    private SignInContract.Controller controller;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    public SignInFragment() {
        controller = new SignInFragmentLocator().createController();
    }

    @Override
    protected SignInContract.Controller getController() {
        return controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
