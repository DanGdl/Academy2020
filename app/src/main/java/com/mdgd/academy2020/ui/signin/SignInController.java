package com.mdgd.academy2020.ui.signin;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.support.auth.AuthViewController;
import com.mdgd.academy2020.cases.UseCase;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.repo.avatar.AvatarRepository;
import com.mdgd.academy2020.models.repo.user.User;
import com.mdgd.academy2020.models.repo.user.UserRepository;
import com.mdgd.academy2020.models.validators.Validator;
import com.mdgd.academy2020.util.Pair;
import com.mdgd.academy2020.util.TextUtil;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class SignInController extends AuthViewController<SignInContract.View> implements SignInContract.Controller {

    private final UseCase<Object, Single<Notification>> logoutUseCase;
    private final AvatarRepository avatarRepo;
    private final UserRepository userRepo;
    private final Network network;
    private final int mode;

    SignInController(int mode, Network network, Prefs prefs, ProfileCache profileCache, Validator<String> emailValidator,
                     Validator<String> passwordValidator, UserRepository userRepo, AvatarRepository avatarRepo,
                     UseCase<Object, Single<Notification>> logoutUseCase) {
        super(emailValidator, passwordValidator, profileCache, prefs);
        this.mode = mode;
        this.logoutUseCase = logoutUseCase;
        this.avatarRepo = avatarRepo;
        this.userRepo = userRepo;
        this.network = network;
    }

    private String checkPasswordVerification(String password, String passwordVerification) {
        return !TextUtil.isEmpty(password) && password.equals(passwordVerification) && hasView() ? null : view.getString(R.string.verification_is_not_same);
    }

    @Override
    public void execSignIn() {
        if (hasView()) {
            if (SignInContract.MODE_SIGN_IN == mode) {
                onDestroyDisposable.add(
                        handleAuthExecution(network.loginNewUser(profileCache.getEmail(), profileCache.getPassword())
                                .subscribeOn(Schedulers.io())
                                .flatMap(result -> {
                                    if (result.isFail()) {
                                        return Single.just(new Result<>(result.error));
                                    } else {
                                        prefs.putAuthToken(result.data);
                                        final User user = profileCache.getUser();
                                        user.setUid(result.data);
                                        return userRepo.save(user);
                                    }
                                })
                        ));
            } else {
                onDestroyDisposable.add(handleChainWithProgress(userRepo.save(profileCache.getUser()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            if (result.isFail()) {
                                if (hasView()) {
                                    view.showError(view.getString(R.string.failed_to_update_user), result.error == null ? "" : result.error.getMessage());
                                }
                            } else {
                                if (hasView()) {
                                    view.showToast(R.string.user_updated);
                                    view.onBackPressed();
                                }
                            }
                        }));
            }
        }
    }

    @Override
    public void setupSubscriptions() {
        onStopDisposable.add(Single.just(new Pair<>(avatarRepo.getTypes(), avatarRepo.getType()))
                .filter(pair -> hasView())
                .subscribe(pair -> view.setAvatarTypes(pair.first, pair.second)));

        if (SignInContract.MODE_SIGN_IN == mode) {
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
        } else {
            onStopDisposable.add(Observable.combineLatest(
                    view.getNickNameObservable()
                            .doOnNext(profileCache::putNickname)
                            .filter(name -> hasView())
                            .map(name -> new Pair<>(!TextUtil.isEmpty(name), profileCache.hasOriginalUser() && !name.equals(profileCache.getOriginalUser().getNickname())))
                            .doOnNext(pair -> view.setNickNameError(pair.first ? null : view.getString(R.string.please_fill_nuckname)))
                            .map(pair -> pair.first && pair.second),

                    profileCache.getImageUrlObservable()
                            .map(url -> profileCache.hasOriginalUser() && !url.equals(profileCache.getOriginalUser().getImageUrl())),

                    (isNickNameValid, isAvatarChanged) -> isNickNameValid || isAvatarChanged)
                    .filter(isEnabled -> hasView())
                    .subscribe(isEnabled -> view.setSignInEnabled(isEnabled)));

            onStopDisposable.add(Single.fromCallable(prefs::getAuthToken)
                    .flatMap(userRepo::getUser)
                    .filter(result -> hasView())
                    .subscribe(result -> {
                        if (result.isFail()) {
                            if (result.error != null) {
                                view.showError(view.getString(R.string.failed_load_user), result.error.getMessage());
                            }
                        } else {
                            profileCache.putOriginalUser(result.data);
                            view.setUser(result.data, avatarRepo.getType());
                        }
                    }));
        }
    }

    @Override
    public void generateImage() {
        onStopDisposable.add(loadAvatar(avatarRepo.generateNewUrl()));
    }

    @Override
    public void setAvatarType(String type) {
        onStopDisposable.add(loadAvatar(avatarRepo.generateNewUrl(type)));
    }

    @Override
    public void logout() {
        onStopDisposable.add(handleChainWithProgress(logoutUseCase.exec(null))
                .filter(event -> hasView())
                .subscribe(event -> view.goToAuth()));
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
