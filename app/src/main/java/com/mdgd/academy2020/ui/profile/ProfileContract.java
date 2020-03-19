package com.mdgd.academy2020.ui.profile;

import com.mdgd.academy2020.arch.Contract;

import io.reactivex.Observable;
import kotlin.Unit;

public class ProfileContract {

    public interface Controller extends Contract.Controller<View> {
        void setupSubscriptions();
    }

    public interface View extends Contract.View {
        Observable<Unit> getLogoutObservable();
    }

    public interface Host extends Contract.Host {
    }
}
