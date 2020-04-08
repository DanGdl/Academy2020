package com.mdgd.academy2020.ui.auth;

import com.mdgd.academy2020.arch.Contract;

public class AuthContract {

    public interface Controller<T extends View> extends Contract.Controller<T> {
    }

    public interface View extends Contract.View {
    }

    public interface Host extends Contract.Host {
        void proceedToLogIn();

        void proceedToSignIn();
    }
}
