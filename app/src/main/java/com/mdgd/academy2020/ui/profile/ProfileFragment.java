package com.mdgd.academy2020.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.rxbinding3.view.RxView;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

import io.reactivex.Observable;
import kotlin.Unit;

public class ProfileFragment extends MvpFragment<ProfileContract.Controller, ProfileContract.Host> implements ProfileContract.View {
    private ProfileContract.Controller controller;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
        controller = new ProfileFragmentLocator().createController();
    }

    @Override
    protected ProfileContract.Controller getController() {
        return controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getController().setupSubscriptions();
    }

    @Override
    public Observable<Unit> getLogoutObservable() {
        return RxView.clicks(getView().findViewById(R.id.profile_logout));
    }
}
