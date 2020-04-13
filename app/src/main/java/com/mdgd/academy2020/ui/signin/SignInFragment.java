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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.repo.user.User;
import com.mdgd.academy2020.util.ImageUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SignInFragment extends MvpFragment<SignInContract.Controller, SignInContract.Host>
        implements SignInContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String KEY_MODE = "mode";

    private SignInContract.Controller controller;
    private EditText passwordVerificationView;
    private EditText nickNameView;
    private EditText passwordView;
    private ImageView avatarView;
    private Spinner avatarType;
    private TextView signInBtn;
    private EditText email;
    private ArrayAdapter<String> avatarTypesAdapter;
    private boolean firstSkipped = false;
    private int mode;

    public static Fragment newSignInInstance() {
        final Bundle args = new Bundle();
        args.putInt(KEY_MODE, SignInContract.MODE_SIGN_IN);
        final SignInFragment f = new SignInFragment();
        f.setArguments(args);
        return f;
    }

    public static Fragment newProfileInstance() {
        final Bundle args = new Bundle();
        args.putInt(KEY_MODE, SignInContract.MODE_PROFILE);
        final SignInFragment f = new SignInFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() == null) {
            mode = SignInContract.MODE_PROFILE;
        } else {
            mode = getArguments().getInt(KEY_MODE);
        }
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
        firstSkipped = false;

        avatarView = view.findViewById(R.id.avatar);
        avatarView.setOnClickListener(this);

        view.findViewById(R.id.retry_avatar).setOnClickListener(this);

        nickNameView = view.findViewById(R.id.nick_name);
        passwordView = view.findViewById(R.id.password);
        passwordView.setVisibility(mode == SignInContract.MODE_SIGN_IN ? View.VISIBLE : View.GONE);

        passwordVerificationView = view.findViewById(R.id.password_verification);
        passwordVerificationView.setVisibility(mode == SignInContract.MODE_SIGN_IN ? View.VISIBLE : View.GONE);

        signInBtn = view.findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(this);
        signInBtn.setText(mode == SignInContract.MODE_SIGN_IN ? R.string.sign_in : R.string.save);

        email = view.findViewById(R.id.email);
        email.setEnabled(mode == SignInContract.MODE_SIGN_IN);

        final View cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        cancel.setVisibility(mode == SignInContract.MODE_PROFILE ? View.VISIBLE : View.GONE);

        final View logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        logout.setVisibility(mode == SignInContract.MODE_PROFILE ? View.VISIBLE : View.GONE);

        avatarTypesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        avatarType = view.findViewById(R.id.profile_avatar_type);
        avatarType.setAdapter(avatarTypesAdapter);
        avatarType.setOnItemSelectedListener(this);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (firstSkipped) {
            getController().setAvatarType((String) parent.getAdapter().getItem(position));
        } else {
            firstSkipped = true;
        }
    }

    @Override
    public void showError(String title, String message) {
        if (hasCallBack()) {
            getCallBack().showError(title, message);
        }
    }

    @Override
    public void setUser(User user) {
        final int position = avatarTypesAdapter.getPosition(user.getAvatarType());
        if (position != -1) {
            firstSkipped = false;
            avatarType.setSelection(position);
        }
        ImageUtil.loadImage(avatarView, user);
        nickNameView.setText(user.getNickname());
        email.setText(user.getEmail());
    }

    @Override
    public void setAvatarTypes(List<String> types) {
        firstSkipped = false;
        avatarTypesAdapter.addAll(types);
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
    public void onNothingSelected(AdapterView<?> parent) {/*empty*/}
}
