package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.support.auth.AuthViewController;
import com.mdgd.academy2020.cases.auth.AuthParams;
import com.mdgd.academy2020.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.avatar.AvatarRepository;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.validators.Validator;
import com.mdgd.academy2020.util.TextUtil;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

class SignInController extends AuthViewController<SignInContract.View> implements SignInContract.Controller {

    private final UserAuthUseCase userAuthUseCase;
    private final AvatarRepository avatarRepo;

    SignInController(ProfileCache profileCache, Validator<String> emailValidator, Validator<String> passwordValidator,
                     UserAuthUseCase userAuthUseCase,
                     AvatarRepository avatarRepo) {
        super(emailValidator, passwordValidator, profileCache);
        this.userAuthUseCase = userAuthUseCase;
        this.avatarRepo = avatarRepo;
    }

    private String checkPasswordVerification(String password, String passwordVerification) {
        return !TextUtil.isEmpty(password) && password.equals(passwordVerification) && hasView() ? null : view.getString(R.string.verification_is_not_same);
    }

    @Override
    public void execSignIn() {
        if (hasView()) {
            onDestroyDisposable.add(
                    handleAuthExecution(userAuthUseCase.exec(AuthParams.newSignInParams(profileCache.getNickname(),
                            profileCache.getEmail(), profileCache.getPassword(), profileCache.getImageUrl())))
            );
        }
    }

    @Override
    public void setupSubscriptions() {
        onStopDisposable.add(Observable.combineLatest(
                getEmailValidationObservable(),

                view.getNickNameObservable()
                        .skip(1)
                        .doOnNext(profileCache::putNickname)
                        .filter(name -> hasView())
                        .map(name -> !TextUtil.isEmpty(name))
                        .doOnNext(isValid -> view.setNickNameError(isValid ? null : view.getString(R.string.please_fill_nuckname))),

                Observable.combineLatest(
                        getPasswordValidationObservable(),

                        view.getPasswordVerificationObservable()
                                .skip(1).filter(result -> hasView()),

                        (result, passwordVerification) -> {
                            final String errorMsg = checkPasswordVerification(result.password, passwordVerification);
                            view.setPasswordVerificationError(errorMsg);
                            return !result.errorMessage.isPresent() && TextUtil.isEmpty(errorMsg);
                        }),

                (isEmailValid, isNickNameValid, arePasswordsValid) -> isEmailValid && isNickNameValid && arePasswordsValid)
                .filter(isEnabled -> hasView())
                .subscribe(isEnabled -> view.setSignInEnabled(isEnabled)));

        onStopDisposable.add(loadAvatar(avatarRepo.getUrl()));
    }

    @Override
    public void generateImage() {
        onStopDisposable.add(loadAvatar(avatarRepo.generateNewUrl()));
    }

    @Override
    public void takePicture() {
        onDestroyDisposable.add(avatarRepo.captureImage(view.showTakePictureScreen())
                .subscribe(this::handleCaptureResult));
    }

    private Disposable loadAvatar(Single<String> source) {
        return source
                .filter(url -> hasView())
                .subscribe(url -> view.loadAvatar(url));
    }

    private void handleCaptureResult(Result<String> result) {
        if (result.isFail() && hasView()) {
            view.showError(view.getString(R.string.failed_to_take_picture), result.error.getMessage());
        } else {
            if (hasView()) {
                view.loadAvatar(result.data);
            }
        }
    }
}
