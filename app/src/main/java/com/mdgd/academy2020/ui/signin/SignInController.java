package com.mdgd.academy2020.ui.signin;

import com.google.common.base.Optional;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.cases.auth.AuthParams;
import com.mdgd.academy2020.models.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.validators.Validator;
import com.mdgd.academy2020.util.TextUtil;

import io.reactivex.Observable;

class SignInController extends MvpController<SignInContract.View> implements SignInContract.Controller {

    private final Validator<String> emailValidator;
    private final Validator<String> passwordValidator;
    private final UserAuthUseCase userAuthUseCase;

    private String imageUrl = "";
    private String nickname = "";
    private String password = "";
    private String email = "";

    SignInController(Validator<String> emailValidator, Validator<String> passwordValidator, UserAuthUseCase userAuthUseCase) {
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.userAuthUseCase = userAuthUseCase;
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
                        .doOnNext(email -> this.email = email)
                        .filter(email -> hasView())
                        .map(emailValidator::validate)
                        .doOnNext(errorMsg -> view.setEmailError(errorMsg.isPresent() ? errorMsg.get() : null))
                        .map(errorMsg -> !errorMsg.isPresent()),

                view.getNickNameObservable()
                        .doOnNext(name -> nickname = name)
                        .filter(name -> hasView())
                        .map(name -> !TextUtil.isEmpty(name))
                        .doOnNext(isValid -> view.setNickNameError(isValid ? null : view.getString(R.string.please_fill_nuckname))),

                Observable.combineLatest(
                        view.getPasswordObservable()
                                .doOnNext(password -> this.password = password)
                                .map(password -> new PasswordValidationResult(password, passwordValidator.validate(password)))
                                .filter(result -> hasView())
                                .doOnNext(result -> view.setPasswordError(result.errorMessage.isPresent() ? result.errorMessage.get() : null)),

                        view.getPasswordVerificationObservable().filter(result -> hasView()),

                        (result, passwordVerification) -> {
                            final String errorMsg = checkPasswordVerification(result.password, passwordVerification);
                            view.setPasswordVerificationError(errorMsg);
                            return !result.errorMessage.isPresent() && TextUtil.isEmpty(errorMsg);
                        }),

                (isEmailValid, isNicknameValid, arePasswordsValid) -> isEmailValid && isNicknameValid && arePasswordsValid)
                .filter(isEnabled -> hasView())
                .subscribe(isEnabled -> view.setSignInEnabled(isEnabled)));

        if (!TextUtil.isEmpty(imageUrl) && hasView()) {
            view.loadAvatar(imageUrl);
        }
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


    private static class PasswordValidationResult {
        final String password;
        final Optional<String> errorMessage;

        PasswordValidationResult(String password, Optional<String> errorMessage) {
            this.password = password;
            this.errorMessage = errorMessage;
        }
    }
}
