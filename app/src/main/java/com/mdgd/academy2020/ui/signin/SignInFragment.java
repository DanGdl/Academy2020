package com.mdgd.academy2020.ui.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.util.ImageUtil;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SignInFragment extends MvpFragment<SignInContract.Controller, SignInContract.Host>
        implements SignInContract.View, View.OnClickListener {

    private SignInContract.Controller controller;
    private ImageView avatarView;
    private EditText nickNameView;
    private EditText passwordView;
    private EditText passwordVerificationView;
    private EditText email;
    private View signInBtn;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    public SignInFragment() {
        setRetainInstance(true);
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
        avatarView = view.findViewById(R.id.avatar);
        avatarView.setOnClickListener(this);
        nickNameView = view.findViewById(R.id.nick_name);
        passwordView = view.findViewById(R.id.password);
        passwordVerificationView = view.findViewById(R.id.password_verification);
        signInBtn = view.findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(this);
        email = view.findViewById(R.id.email);
    }

    @Override
    public void onStart() {
        super.onStart();
        getController().setupSubscriptions();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (R.id.avatar == id) {
            getController().takePicture();
        } else if (R.id.sign_in_btn == id) {
            getController().execSignIn();
        }
    }

    @Override
    public void showError(String title, String message) {
        if (hasCallBack()) {
            getCallBack().showError(title, message);
        }
    }

    @Override
    public void proceedToLobby() {
        if (hasCallBack()) {
            getCallBack().proceedToLobby();
        }
    }

    @Override
    public Observable<String> getNickNameObservable() {
        return createAfterChangeObservable(nickNameView);
    }

    private Observable<String> createAfterChangeObservable(EditText editText) {
        return RxTextView.afterTextChangeEvents(editText)
                .filter(event -> event.component2() != null)
                .map(event -> event.component2().toString());
    }

    @Override
    public void setNickNameError(String errorMessage) {
        nickNameView.setError(errorMessage);
    }

    @Override
    public Observable<String> getPasswordObservable() {
        return createAfterChangeObservable(passwordView);
    }

    @Override
    public void setPasswordError(String errorMsg) {
        passwordView.setError(errorMsg);
    }

    @Override
    public Observable<String> getPasswordVerificationObservable() {
        return createAfterChangeObservable(passwordVerificationView);
    }

    @Override
    public void setSignInEnabled(Boolean isEnabled) {
        signInBtn.setEnabled(isEnabled);
    }

    @Override
    public void setPasswordVerificationError(String errorMsg) {
        passwordVerificationView.setError(errorMsg);
    }

    @Override
    public Single<Result<String>> showTakePictureScreen() {
        if (hasCallBack()) {
            return getCallBack().showTakePictureScreen();
        }
        return Single.never();
    }

    @Override
    public void loadAvatar(String data) {
        ImageUtil.loadImage(avatarView, data);
    }

    @Override
    public Observable<String> getEmailObservable() {
        return createAfterChangeObservable(email);
    }

    @Override
    public void setEmailError(String errorMessage) {
        email.setError(errorMessage);
    }
}
