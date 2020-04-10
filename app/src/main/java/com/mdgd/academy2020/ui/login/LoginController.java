package com.mdgd.academy2020.ui.login;

import com.mdgd.academy2020.arch.support.auth.AuthViewController;
import com.mdgd.academy2020.arch.support.auth.PasswordValidationResult;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.repo.user.UserRepository;
import com.mdgd.academy2020.models.validators.Validator;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

class LoginController extends AuthViewController<LoginContract.View> implements LoginContract.Controller {

    private final UserRepository userRepository;
    private final Network network;

    LoginController(Validator<String> emailValidator, Validator<String> passwordValidator,
                    ProfileCache profileCache, UserRepository userRepository, Network network, Prefs prefs) {
        super(emailValidator, passwordValidator, profileCache, prefs);
        this.userRepository = userRepository;
        this.network = network;
    }

    @Override
    public void execLogIn() {
        onDestroyDisposable.add(
                handleAuthExecution(
                        network.loginUser(profileCache.getEmail(), profileCache.getPassword())
                                .subscribeOn(Schedulers.io())
                                .flatMap(result -> {
                                    if (result.isFail()) {
                                        return Single.just(new Result<>(result.error));
                                    } else {
                                        prefs.putAuthToken(result.data);
                                        return userRepository.loadUser(result.data);
                                    }
                                })
                )
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
