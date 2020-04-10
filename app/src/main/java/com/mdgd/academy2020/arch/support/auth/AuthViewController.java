package com.mdgd.academy2020.arch.support.auth;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.validators.Validator;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class AuthViewController<T extends AuthContract.View> extends MvpController<T> implements AuthContract.Controller<T> {

    protected final ProfileCache profileCache;
    protected final Prefs prefs;
    private final Validator<String> passwordValidator;
    private final Validator<String> emailValidator;

    public AuthViewController(Validator<String> emailValidator, Validator<String> passwordValidator, ProfileCache profileCache, Prefs prefs) {
        this.passwordValidator = passwordValidator;
        this.emailValidator = emailValidator;
        this.profileCache = profileCache;
        this.prefs = prefs;
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

    protected <T1> Disposable handleAuthExecution(Single<Result<T1>> chain) {
        return handleChainWithProgress(chain)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (result.isFail() && hasView()) {
                        if (result.error == null) {
                            view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                        } else {
                            result.error.printStackTrace();
                            view.showError(view.getString(R.string.login_failed), result.error.getMessage());
                        }
                    } else {
                        if (hasView()) {
                            view.showToast(R.string.login_successful);
                            view.proceedToLobby();
                        }
                    }
                }, error -> {
                    error.printStackTrace();
                    if (hasView()) {
                        view.hideProgress();
                        view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                    }
                });
    }
}
