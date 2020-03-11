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

import io.reactivex.Observable;

public class LoginFragment extends MvpFragment<LoginContract.Controller, LoginContract.Host> implements LoginContract.View, View.OnClickListener {
    private LoginContract.Controller controller;
    private EditText emailEditText;
    private EditText passwordEditText;
    private View loginBtn;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() {
        setRetainInstance(true);
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
        loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
    }

    @Override
    public void onStart() {
        super.onStart();
        getController().setupSubscriptions();
    }

    @Override
    public void onClick(View v) {
        if (R.id.login_btn == v.getId()) {
            controller.execLogIn();
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

    @Override
    public Observable<String> getEmailObservable() {
        return createAfterChangeObservable(emailEditText);
    }

    @Override
    public void setEmailError(String errorMsg) {
        emailEditText.setError(errorMsg);
    }

    @Override
    public Observable<String> getPasswordObservable() {
        return createAfterChangeObservable(passwordEditText);
    }

    @Override
    public void setPasswordError(String errorMsg) {
        passwordEditText.setError(errorMsg);
    }

    @Override
    public void setLoginEnabled(Boolean isEnabled) {
        loginBtn.setEnabled(isEnabled);
    }

    private Observable<String> createAfterChangeObservable(EditText editText) {
        return RxTextView.afterTextChangeEvents(editText)
                .filter(event -> event.component2() != null)
                .map(event -> event.component2().toString());
    }
}
