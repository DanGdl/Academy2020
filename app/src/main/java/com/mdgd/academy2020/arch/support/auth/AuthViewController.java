package com.mdgd.academy2020.arch.support.auth;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.cases.auth.AuthResult;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.validators.Validator;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class AuthViewController<T extends AuthContract.View> extends MvpController<T> implements AuthContract.Controller<T> {

    private final Validator<String> emailValidator;
    private final Validator<String> passwordValidator;
    protected final ProfileCache profileCache;

    public AuthViewController(Validator<String> emailValidator, Validator<String> passwordValidator, ProfileCache profileCache) {
        this.emailValidator = emailValidator;
        this.passwordValidator = passwordValidator;
        this.profileCache = profileCache;
    }

    protected Observable<Boolean> getEmailValidationObservable() {
        return view.getEmailObservable()
                .skip(1)
                .doOnNext(profileCache::putEmail)
                .map(emailValidator::validate)
                .filter(errorMsg -> hasView())
                .map(errorMsg -> {
                    view.setEmailError(errorMsg.isPresent() ? errorMsg.get() : null);
                    return !errorMsg.isPresent();
                });
    }

    protected Observable<PasswordValidationResult> getPasswordValidationObservable() {
        return view.getPasswordObservable()
                .skip(1)
                .doOnNext(profileCache::putPassword)
                .map(password -> new PasswordValidationResult(password, passwordValidator.validate(password)))
                .filter(result -> hasView())
                .doOnNext(result -> view.setPasswordError(result.errorMessage.isPresent() ? result.errorMessage.get() : null));
    }

    protected Disposable handleAuthExecution(Single<Result<AuthResult>> chain) {
        return handleChainWithProgress(chain)
                .subscribe(result -> {
                    if (result.isFail() && hasView()) {
                        if (result.error == null) {
                            view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                        } else {
                            view.showError(view.getString(R.string.login_failed), result.error.getMessage());
                        }
                    } else {
                        if (hasView()) {
                            view.showToast(R.string.login_successful);
                            view.proceedToLobby();
                        }
                    }
                }, error -> {
                    if (hasView()) {
                        view.hideProgress();
                        view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                    }
                });
    }
}
