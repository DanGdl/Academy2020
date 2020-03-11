package com.mdgd.academy2020.models.cases.auth;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.models.cases.UseCase;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserAuthUseCase implements UseCase<AuthParams, Disposable> {

    private final Network network;
    private final Prefs prefs;

    public UserAuthUseCase(Network network, Prefs prefs) {
        this.network = network;
        this.prefs = prefs;
    }

    @Override
    public Disposable exec(AuthParams params) {
        final Single<Result<String>> single;
        switch (params.getType()) {
            case AuthParams.TYPE_SIGN_IN: {
                single = network.createNewUser(params.getNickName(), params.getEmail(), params.getPassword(), params.getImageUrl());
            }
            break;

            case AuthParams.TYPE_LOG_IN: {
                single = network.execLogin(params.getEmail(), params.getPassword());
            }
            break;

            default:
                single = Single.never();
        }
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (params.hasView()) {
                        params.getView().showProgress();
                    }
                })
                .doFinally(() -> {
                    if (params.hasView()) {
                        params.getView().hideProgress();
                    }
                })
                .subscribe(result -> {
                    final AuthView view = params.getView();
                    if (result.isFail() && params.hasView()) {
                        if (result.error == null) {
                            view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                        } else {
                            view.showError(view.getString(R.string.login_failed), result.error.getMessage());
                        }
                    } else {
                        // todo save image url
                        prefs.putAuthToken(result.data);
                        if (params.hasView()) {
                            view.showToast(R.string.login_successful);
                            view.proceedToLobby();
                        }
                    }
                }, error -> {
                    if (params.hasView()) {
                        final AuthView view = params.getView();
                        view.hideProgress();
                        view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                    }
                });
    }
}
