package com.mdgd.academy2020.arch;

import androidx.annotation.CallSuper;

import io.reactivex.disposables.CompositeDisposable;

/**
 * minimal implementation of presenter
 *
 * @param <V> - interface of activity/fragment/service/task
 */
public abstract class MvpController<V extends Contract.View> implements Contract.Controller<V> {
    protected final CompositeDisposable onStopDisposable = new CompositeDisposable();
    protected final CompositeDisposable onDestroyDisposable = new CompositeDisposable();

    /**
     * link to activity/fragment/service/task
     */
    protected V view;

    @Override
    @CallSuper
    public final void subscribe(V view) {
        this.view = view;
    }

    @Override
    @CallSuper
    public void unsubscribe() {
        onStopDisposable.clear();
        view = null;
    }

    protected boolean hasView() {
        return view != null;
    }

    @Override
    @CallSuper
    public void destroy() {
        onDestroyDisposable.clear();
    }
}
