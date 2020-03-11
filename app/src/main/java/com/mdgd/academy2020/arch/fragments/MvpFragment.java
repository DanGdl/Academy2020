package com.mdgd.academy2020.arch.fragments;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.Contract;

import java.lang.reflect.ParameterizedType;

import io.reactivex.disposables.CompositeDisposable;


public abstract class MvpFragment<ControllerType extends Contract.Controller, Host extends Contract.Host>
        extends Fragment implements Contract.View {

    protected final CompositeDisposable onStopDisposable = new CompositeDisposable();
    protected final CompositeDisposable onDestroyDisposable = new CompositeDisposable();
    private boolean isStarted = false;
    /**
     * the fragment callBack
     */
    private Host callBack;

    //@Override
    public final boolean hasCallBack() {
        return callBack != null;
    }

    public final boolean noHost() {
        return callBack == null;
    }

    /**
     * get the current fragment call back
     *
     * @return the current fragment call back
     */
    public final Host getCallBack() {
        return callBack;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // keep the call back
        try {
            this.callBack = (Host) context;
        } catch (Throwable e) {
            final String hostClassName = ((Class) ((ParameterizedType) getClass().
                    getGenericSuperclass())
                    .getActualTypeArguments()[1]).getCanonicalName();
            throw new RuntimeException("Activity must implement " + hostClassName
                    + " to attach " + this.getClass().getSimpleName(), e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // release the call back
        this.callBack = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        final ControllerType controllerType = getController();
        if (controllerType != null) {
            controllerType.subscribe(this);
        }
        isStarted = true;
    }

    @Override
    public void onStop() {
        isStarted = false;
        final ControllerType controllerType = getController();
        if (controllerType != null) {
            controllerType.unsubscribe();
        }
        onStopDisposable.clear();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        final ControllerType controllerType = getController();
        if (controllerType != null) {
            controllerType.destroy();
        }
        onDestroyDisposable.clear();
        super.onDestroy();
    }

    protected abstract ControllerType getController();

    @Override
    public void showProgress() {
        if (hasCallBack()) {
            getCallBack().showProgress(R.string.empty, R.string.empty);
        }
    }

    @Override
    public void showProgress(String title, String message) {
        if (hasCallBack()) {
            getCallBack().showProgress(title, message);
        }
    }

    @Override
    public void hideProgress() {
        if (hasCallBack()) {
            getCallBack().hideProgress();
        }
    }

    @Override
    public void showToast(int strResId, Object... args) {
        Toast.makeText(getActivity(), getString(strResId, args), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }
}
