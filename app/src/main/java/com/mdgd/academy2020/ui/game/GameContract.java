package com.mdgd.academy2020.ui.game;

import com.mdgd.academy2020.arch.Contract;

public class GameContract {

    public interface Controller extends Contract.Controller<View> {
    }

    public interface View extends Contract.View {
    }

    public interface Host extends Contract.Host {
    }
}
