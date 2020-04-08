package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.arch.support.auth.AuthViewController;
import com.mdgd.academy2020.arch.support.auth.PasswordValidationResult;
import com.mdgd.academy2020.cases.auth.AuthParams;
import com.mdgd.academy2020.cases.auth.UserAuthUseCase;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.validators.Validator;

import io.reactivex.Observable;

class LoginController extends AuthViewController<LoginContract.View> implements LoginContract.Controller {

    private final UserAuthUseCase userAuthUseCase;

    LoginController(Validator<String> emailValidator, Validator<String> passwordValidator,
                    UserAuthUseCase userAuthUseCase, ProfileCache profileCache) {
        super(emailValidator, passwordValidator, profileCache);
        this.userAuthUseCase = userAuthUseCase;
    }

    @Override
    public void execLogIn() {
        onDestroyDisposable.add(
                handleAuthExecution(userAuthUseCase.exec(AuthParams.newLogInParams(profileCache.getEmail(), profileCache.getPassword())))
        );
    }

    @Override
    public void setupSubscriptions() {
        onStopDisposable.add(
                Observable.combineLatest(
                        getEmailValidationObservable(),
                        getPasswordValidationObservable()
                                .map(PasswordValidationResult::isValid),

                        (isEmailValid, isPasswordValid) -> isEmailValid && isPasswordValid
                )
                        .filter(isValid -> hasView())
                        .subscribe(isValid -> view.setLoginEnabled(isValid)));
    }
}
