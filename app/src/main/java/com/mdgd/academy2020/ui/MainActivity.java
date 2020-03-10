package com.mdgd.academy2020.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.HostActivity;
import com.mdgd.academy2020.arch.progress.ProgressContainer;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.ui.auth.AuthContract;
import com.mdgd.academy2020.ui.auth.AuthFragment;
import com.mdgd.academy2020.ui.error.ErrorFragment;
import com.mdgd.academy2020.ui.lobby.LobbyFragment;
import com.mdgd.academy2020.ui.login.LoginContract;
import com.mdgd.academy2020.ui.login.LoginFragment;
import com.mdgd.academy2020.ui.progress.ProgressDialogWrapper;
import com.mdgd.academy2020.ui.signin.SignInContract;
import com.mdgd.academy2020.ui.signin.SignInFragment;
import com.mdgd.academy2020.ui.splash.SplashContract;
import com.mdgd.academy2020.ui.splash.SplashFragment;
import com.mdgd.academy2020.util.PermissionsUtil;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends HostActivity implements ProgressContainer, SplashContract.Host,
        AuthContract.Host, LoginContract.Host, SignInContract.Host {
    private static final int RC_IMAGE_PERMISSIONS = 10265;
    private static final int PICK_IMAGE_CHOOSER_REQUEST_CODE = 48556;

    private final PublishSubject<Result<String>> onCaptureImageSubject = PublishSubject.create();
    private ProgressDialogWrapper progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragment(SplashFragment.newInstance());
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    @Override
    public boolean hasProgress() {
        return true;
    }

    @Override
    public void showProgress(String title, String message) {
        if (progress == null) {
            progress = new ProgressDialogWrapper(this, title, message);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void showProgress(int titleRes, int messageRes) {
        if (progress == null) {
            progress = new ProgressDialogWrapper(this, titleRes, messageRes);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialogWrapper(this);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }

    @Override
    public void proceedToLobby() {
        replaceFragment(LobbyFragment.newInstance());
    }

    @Override
    public void showError(String title, String message) {
        ErrorFragment.newInstance(title, message).show(getSupportFragmentManager(), "error");
    }

    @Override
    public void proceedToAuth() {
        replaceFragment(AuthFragment.newInstance());
    }

    @Override
    public void proceedToLogIn() {
        addFragmentToBackStack(LoginFragment.newInstance());
    }

    @Override
    public void proceedToSignIn() {
        addFragmentToBackStack(SignInFragment.newInstance());
    }

    @Override
    public Single<Result<String>> showTakePictureScreen() {
        if (PermissionsUtil.requestPermissionsIfNeed(this, RC_IMAGE_PERMISSIONS, READ_EXTERNAL_STORAGE, CAMERA)) {
            final Intent pickImageChooserIntent = CropImage.getPickImageChooserIntent(this);
            if (getPackageManager().resolveActivity(pickImageChooserIntent, 0) == null) {
                findViewById(getFragmentContainerId()).postDelayed(() -> onCaptureImageSubject.onNext(new Result<>(new Exception("There is no activity to handle image selection"))), 500);
            } else {
                startActivityForResult(CropImage.getPickImageChooserIntent(this), PICK_IMAGE_CHOOSER_REQUEST_CODE);
            }
        }
        return onCaptureImageSubject.firstOrError();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_IMAGE_PERMISSIONS) {
            if (PermissionsUtil.areAllPermissionsGranted(grantResults)) {
                showTakePictureScreen();
            } else {
                onCaptureImageSubject.onNext(new Result<>(new Exception("Permissions not granted")));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PICK_IMAGE_CHOOSER_REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            cropImage(data);
        }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            final Uri output = UCrop.getOutput(data);
            if (output != null) {
                onCaptureImageSubject.onNext(new Result<>(output.toString()));
            }
        }
    }

    private void cropImage(Intent result) {
        final Uri imageUri = CropImage.getPickImageResultUri(this, result);

        final UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setToolbarColor(Color.WHITE);
        options.setStatusBarColor(Color.BLACK);
        options.setCircleDimmedLayer(true);

        final File destinationFile = new File(getCacheDir(), String.valueOf(System.currentTimeMillis()));
        final Intent intent = UCrop.of(imageUri, Uri.fromFile(destinationFile))
                .withOptions(options)
                .withMaxResultSize(512, 512)
                .withAspectRatio(5, 5)
                .getIntent(this);

        startActivityForResult(intent, UCrop.REQUEST_CROP);
    }
}
