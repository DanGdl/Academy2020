package com.mdgd.academy2020.ui.splash;

import com.mdgd.academy2020.arch.MvpController;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.util.TextUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

class SplashController extends MvpController<SplashContract.View> implements SplashContract.Controller {
    private final Prefs prefs;

    public SplashController(Prefs prefs) {
        this.prefs = prefs;
    }

    @Override
    public void checkUserStatus() {
        onStopDisposable.add(Single.just(prefs.getAuthToken())
                .delay(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(token -> !TextUtil.isEmpty(token))
                .filter(hasToken -> hasView())
                .subscribe(hasToken -> {
                    if (hasToken) {
                        view.proceedToLobby();
                    } else {
                        view.proceedToAuth();
                    }
                }));
    }
}
