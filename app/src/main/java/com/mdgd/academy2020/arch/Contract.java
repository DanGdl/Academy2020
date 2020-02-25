package com.mdgd.academy2020.arch;

/**
 * basic contract for mvp/mvi/mvvm
 */
public class Contract {

    public interface Controller<V extends View> {
        /**
         * call in onStart()
         *
         * @param view - activity/fragment/view
         */
        void subscribe(V view);

        /**
         * call in onStop()
         */
        void unsubscribe();

        void destroy();
    }

    public interface View {
    }

    public interface Host {
    }
}
