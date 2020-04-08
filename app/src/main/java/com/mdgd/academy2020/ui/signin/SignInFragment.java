package com.mdgd.academy2020.ui.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.util.ImageUtil;

import io.reactivex.Observable;
import io.reactivex.Single;

// todo: add button cancel and logout in profile mode, add spinner for avatar. make email and password not editable, fill data
public class SignInFragment extends MvpFragment<SignInContract.Controller, SignInContract.Host>
        implements SignInContract.View, View.OnClickListener {

    private static final String KEY_MODE = "mode";
    private static final int MODE_SIGN_IN = 1;
    private static final int MODE_PROFILE = 2;

    private SignInContract.Controller controller;
    private EditText passwordVerificationView;
    private EditText nickNameView;
    private EditText passwordView;
    private ImageView avatarView;
    private EditText email;
    private View signInBtn;

    public static Fragment newSignInInstance() {
        final Bundle args = new Bundle();
        args.putInt(KEY_MODE, MODE_SIGN_IN);
        final SignInFragment f = new SignInFragment();
        f.setArguments(args);
        return f;
    }

    public static Fragment newProfileInstance() {
        final Bundle args = new Bundle();
        args.putInt(KEY_MODE, MODE_PROFILE);
        final SignInFragment f = new SignInFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        controller = new SignInFragmentLocator().createController(getArguments().getInt(KEY_MODE));
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
        view.findViewById(R.id.retry_avatar).setOnClickListener(this);
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
        if (R.id.retry_avatar == id) {
            getController().generateImage();
        } else if (R.id.avatar == id) {
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
