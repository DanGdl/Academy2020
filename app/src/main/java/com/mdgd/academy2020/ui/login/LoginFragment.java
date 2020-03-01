package com.mdgd.academy2020.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;
import com.mdgd.academy2020.util.TextUtil;

import io.reactivex.Observable;

public class LoginFragment extends MvpFragment<LoginContract.Controller, LoginContract.Host> implements LoginContract.View, View.OnClickListener {
    private LoginContract.Controller controller;
    private EditText emailEditText;
    private EditText passwordEditText;

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
        final View loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);

        // todo move to presenter
        onStopDisposables.add(Observable.combineLatest(
                RxTextView.afterTextChangeEvents(emailEditText)
                        .filter(event -> event.component2() != null)
                        .map(event -> getController().validateEmail(event.component2().toString()))
                        .map(errorMsg -> {
                            emailEditText.setError(errorMsg);
                            return TextUtil.isEmpty(errorMsg);
                        }),
                RxTextView.afterTextChangeEvents(passwordEditText)
                        .filter(event -> event.component2() != null)
                        .map(event -> getController().validatePassword(event.component2().toString()))
                        .map(errorMsg -> {
                            passwordEditText.setError(errorMsg);
                            return TextUtil.isEmpty(errorMsg);
                        }), (isEmailValid, isPasswordValid) -> isEmailValid && isPasswordValid)
                .subscribe(loginBtn::setEnabled));
    }

    @Override
    public void onClick(View v) {
        if (R.id.login_btn == v.getId()) {
            controller.execLogIn(getText(emailEditText), getText(passwordEditText));
        }
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }

    @Override
    public void proceedToLobby() {
        if (hasCallBack()) {
            getCallBack().proceedToLobby();
        }
    }

    @Override
    public void showError(String title, String message) {
        if (hasCallBack()) {
            getCallBack().showError(title, message);
        }
    }
}
