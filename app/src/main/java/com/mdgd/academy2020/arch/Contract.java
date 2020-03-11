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
        String getString(int strResId, Object... args);

        void showToast(int strResId, Object... args);

        void showProgress();

        void showProgress(String title, String message);

        void hideProgress();

        boolean isStarted();
    }

    public interface Host {
        void showProgress(int titleResId, int messageResId);

        void showProgress(String title, String message);

        void hideProgress();
    }
}
