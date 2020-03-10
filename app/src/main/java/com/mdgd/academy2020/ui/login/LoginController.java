package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.cases.auth.AuthParams;
import com.mdgd.academy2020.models.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.validators.Validator;
import com.mdgd.academy2020.util.TextUtil;

import io.reactivex.Observable;

class LoginController extends MvpController<LoginContract.View> implements LoginContract.Controller {

    private final Validator<String> emailValidator;
    private final Validator<String> passwordValidator;
    private final UserAuthUseCase userAuthUseCase;

    private String password = "";
    private String email = "";

    LoginController(Validator<String> emailValidator, Validator<String> passwordValidator, UserAuthUseCase userAuthUseCase) {
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.userAuthUseCase = userAuthUseCase;
    }

    @Override
    public void execLogIn() {
        onStopDisposable.add(userAuthUseCase.exec(AuthParams.newLogInParams(email, password, view)));
    }

    @Override
    public void setupSubscriptions() {
        onStopDisposable.add(Observable.combineLatest(
                view.getEmailObservable()
                        .doOnNext(email -> this.email = email)
                        .map(emailValidator::validate)
                        .filter(errorMsg -> hasView())
                        .map(errorMsg -> {
                            view.setEmailError(errorMsg);
                            return TextUtil.isEmpty(errorMsg);
                        }),
                view.getPasswordObservable()
                        .doOnNext(password -> this.password = password)
                        .map(passwordValidator::validate)
                        .filter(errorMsg -> hasView())
                        .map(errorMsg -> {
                            view.setPasswordError(errorMsg);
                            return TextUtil.isEmpty(errorMsg);
                        }), (isEmailValid, isPasswordValid) -> isEmailValid && isPasswordValid)
                .filter(isEnabled -> hasView())
                .subscribe(view::setLoginEnabled));
    }
}
