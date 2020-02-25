package com.mdgd.academy2020.ui.error;

import com.mdgd.academy2020.arch.Contract;

public class ErrorContract {

    public interface Controller extends Contract.Controller<View> {
    }

    public interface View extends Contract.View {
    }

    public interface Host extends Contract.Host {
    }
}
