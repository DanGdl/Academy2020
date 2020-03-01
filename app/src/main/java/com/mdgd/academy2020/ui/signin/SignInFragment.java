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
import com.mdgd.academy2020.util.ImageUtil;
import com.mdgd.academy2020.util.TextUtil;

import io.reactivex.Observable;

public class SignInFragment extends MvpFragment<SignInContract.Controller, SignInContract.Host>
        implements SignInContract.View, View.OnClickListener {

    private SignInContract.Controller controller;
    private ImageView avatarView;
    private EditText nickNameView;
    private EditText passwordView;
    private EditText passwordVerificationView;
    private View signInBtn;

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
        avatarView = view.findViewById(R.id.avatar);
        avatarView.setOnClickListener(this);
        nickNameView = view.findViewById(R.id.nick_name);
        passwordView = view.findViewById(R.id.password);
        passwordVerificationView = view.findViewById(R.id.password_verification);
        signInBtn = view.findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(this);

        // todo move to presenter
        onStopDisposables.add(Observable.combineLatest(
                RxTextView.afterTextChangeEvents(nickNameView)
                        .filter(event -> event.component2() != null)
                        .map(event -> {
                            final boolean empty = TextUtil.isEmpty(event.component2().toString());
                            nickNameView.setError(empty ? getString(R.string.please_fill_nuckname) : null);
                            return empty;
                        }),
                RxTextView.afterTextChangeEvents(passwordView)
                        .filter(event -> event.component2() != null)
                        .map(event -> getController().validatePassword(event.component2().toString()))
                        .map(errorMsg -> {
                            passwordView.setError(errorMsg);
                            return TextUtil.isEmpty(errorMsg);
                        }),
                RxTextView.afterTextChangeEvents(passwordVerificationView)
                        .filter(event -> event.component2() != null)
                        .map(event -> getController().checkPasswordVerification(passwordView.getText().toString(), event.component2().toString()))
                        .map(errorMsg -> {
                            passwordVerificationView.setError(errorMsg);
                            return TextUtil.isEmpty(errorMsg);
                        }), (isNicknameValid, isEmailValid, isPasswordValid) -> isNicknameValid && isEmailValid && isPasswordValid)
                .subscribe(signInBtn::setEnabled));
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (R.id.avatar == id) {
            if (hasCallBack()) {
                // todo move to presenter
                onStopDisposables.add(getCallBack().showTakePictureScreen().subscribe(result -> {
                    if (result.isFail()) {

                    } else {
                        ImageUtil.loadImage(avatarView, result.data);
                    }
                }));
            }
        } else if (R.id.sign_in_btn == id) {
            getController().execSignIn();
        }
    }
}
