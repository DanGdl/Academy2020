package com.mdgd.academy2020.ui.signin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

// todo: fill data
public class SignInFragment extends MvpFragment<SignInContract.Controller, SignInContract.Host>
        implements SignInContract.View, View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String KEY_MODE = "mode";
    private static final int MODE_SIGN_IN = 1;
    private static final int MODE_PROFILE = 2;

    private SignInContract.Controller controller;
    private EditText passwordVerificationView;
    private EditText nickNameView;
    private EditText passwordView;
    private ImageView avatarView;
    private Spinner avatarType;
    private EditText email;
    private View signInBtn;
    private int mode;

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
        mode = getArguments().getInt(KEY_MODE);
        controller = new SignInFragmentLocator().createController(mode);
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
        passwordView.setVisibility(mode == MODE_SIGN_IN ? View.VISIBLE : View.GONE);

        passwordVerificationView = view.findViewById(R.id.password_verification);
        passwordVerificationView.setVisibility(mode == MODE_SIGN_IN ? View.VISIBLE : View.GONE);

        signInBtn = view.findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(this);

        email = view.findViewById(R.id.email);
        email.setEnabled(mode == MODE_SIGN_IN);

        final View cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        cancel.setVisibility(mode == MODE_PROFILE ? View.VISIBLE : View.GONE);

        final View logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        logout.setVisibility(mode == MODE_PROFILE ? View.VISIBLE : View.GONE);

        avatarType = view.findViewById(R.id.profile_avatar_type);
        final String[] stringArray = view.getContext().getResources().getStringArray(R.array.avatar_types);
        avatarType.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, stringArray));
        avatarType.setOnItemClickListener(this);
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
        } else if (R.id.logout == id) {
            getController().logout();
        } else if (R.id.cancel == id && hasCallBack()) {
            getCallBack().onBackPressed();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getController().setAvatarType((String) parent.getAdapter().getItem(position));
    }
}
