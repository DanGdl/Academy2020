package com.mdgd.academy2020.arch.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.mdgd.academy2020.arch.Contract;

import java.lang.reflect.ParameterizedType;


public abstract class MvpFragment<ControllerType extends Contract.Controller, Host extends Contract.Host>
        extends Fragment implements Contract.View {

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
    }

    @Override
    public void onStop() {
        final ControllerType controllerType = getController();
        if (controllerType != null) {
            controllerType.unsubscribe();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        final ControllerType controllerType = getController();
        if (controllerType != null) {
            controllerType.destroy();
        }
        super.onDestroy();
    }

    protected abstract ControllerType getController();
}
