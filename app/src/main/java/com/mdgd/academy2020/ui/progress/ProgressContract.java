package com.mdgd.academy2020.ui.progress;

import com.mdgd.academy2020.arch.Contract;

public class ProgressContract {

    public interface Controller extends Contract.Controller<View> {
    }

    public interface View extends Contract.View {
    }

    public interface Host extends Contract.Host {
    }
}
