package com.mdgd.academy2020.ui.signin;

import com.google.common.base.Optional;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.avatars.AvatarUrlGenerator;
import com.mdgd.academy2020.models.cases.auth.AuthParams;
import com.mdgd.academy2020.models.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.validators.Validator;
import com.mdgd.academy2020.util.TextUtil;

import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class SignInController extends MvpController<SignInContract.View> implements SignInContract.Controller {

    private final AvatarUrlGenerator avatarUrlGenerator;
    private final Validator<String> passwordValidator;
    private final Validator<String> emailValidator;
    private final UserAuthUseCase userAuthUseCase;
    private final Prefs prefs;

    private String imageUrl = "";
    private String nickname = "";
    private String password = "";
    private String email = "";

    SignInController(Validator<String> emailValidator, Validator<String> passwordValidator,
                     UserAuthUseCase userAuthUseCase, Prefs prefs, AvatarUrlGenerator avatarUrlGenerator) {
        this.emailValidator = emailValidator;
        this.prefs = prefs;
        this.passwordValidator = passwordValidator;
        this.userAuthUseCase = userAuthUseCase;
        this.avatarUrlGenerator = avatarUrlGenerator;
    }

    private String checkPasswordVerification(String password, String passwordVerification) {
        return !TextUtil.isEmpty(password) && password.equals(passwordVerification) && hasView() ? null : view.getString(R.string.verification_is_not_same);
    }

    @Override
    public void execSignIn() {
        if (hasView()) {
            onStopDisposable.add(userAuthUseCase.exec(AuthParams.newSignInParams(nickname, email, password, imageUrl, view)));
        }
    }

    @Override
    public void setupSubscriptions() {
        onStopDisposable.add(Observable.combineLatest(
                view.getEmailObservable()
                        .skip(1)
                        .doOnNext(email -> this.email = email)
                        .filter(email -> hasView())
                        .map(emailValidator::validate)
                        .doOnNext(errorMsg -> view.setEmailError(errorMsg.isPresent() ? errorMsg.get() : null))
                        .map(errorMsg -> !errorMsg.isPresent()),

                view.getNickNameObservable()
                        .skip(1)
                        .doOnNext(name -> nickname = name)
                        .filter(name -> hasView())
                        .map(name -> !TextUtil.isEmpty(name))
                        .doOnNext(isValid -> view.setNickNameError(isValid ? null : view.getString(R.string.please_fill_nuckname))),

                Observable.combineLatest(
                        view.getPasswordObservable()
                                .skip(1)
                                .doOnNext(password -> this.password = password)
                                .map(password -> new PasswordValidationResult(password, passwordValidator.validate(password)))
                                .filter(result -> hasView())
                                .doOnNext(result -> view.setPasswordError(result.errorMessage.isPresent() ? result.errorMessage.get() : null)),

                        view.getPasswordVerificationObservable()
                                .skip(1).filter(result -> hasView()),

                        (result, passwordVerification) -> {
                            final String errorMsg = checkPasswordVerification(result.password, passwordVerification);
                            view.setPasswordVerificationError(errorMsg);
                            return !result.errorMessage.isPresent() && TextUtil.isEmpty(errorMsg);
                        }),

                (isEmailValid, isNicknameValid, arePasswordsValid) -> isEmailValid && isNicknameValid && arePasswordsValid)
                .filter(isEnabled -> hasView())
                .subscribe(isEnabled -> view.setSignInEnabled(isEnabled)));

        onStopDisposable.add(loadAvatar(Single.just(imageUrl)
                .flatMap(url -> TextUtil.isEmpty(url) ? checkAvatarHash() : Single.just(url))));
    }

    private Disposable loadAvatar(Single<String> source) {
        return source.doOnEvent((s, throwable) -> imageUrl = s)
                // todo check if file loaded and load if need
                .filter(url -> hasView())
                .subscribe(url -> view.loadAvatar(url));
    }

    private Single<String> checkAvatarHash() {
        return Single.just(prefs.getAvatarHash())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(hash -> {
                    if (TextUtil.isEmpty(hash)) {
                        return generateHash();
                    }
                    return Single.just(hash);
                }).map(avatarUrlGenerator::generate);
    }

    private Single<String> generateHash() {
        return Single.just(new Random().nextInt((int) 10E6))
                .map(String::valueOf)
                .flatMap(hash -> Completable.fromAction(() -> prefs.putImageHash(hash))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .andThen(Single.just(hash)));
    }

    @Override
    public void takePicture() {
        onDestroyDisposable.add(view.showTakePictureScreen().subscribe(result -> {
            if (result.isFail() && hasView()) {
                view.showError(view.getString(R.string.failed_to_take_picture), result.error.getMessage());
            } else {
                imageUrl = result.data;
                if (hasView()) {
                    view.loadAvatar(result.data);
                }
            }
        }));
    }

    @Override
    public void generateImage() {
        onStopDisposable.add(loadAvatar(generateHash()
                .map(avatarUrlGenerator::generate)));
    }


    private static class PasswordValidationResult {
        final String password;
        final Optional<String> errorMessage;

        PasswordValidationResult(String password, Optional<String> errorMessage) {
            this.password = password;
            this.errorMessage = errorMessage;
        }
    }
}
