package com.mdgd.academy2020.ui.lobby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

public class LobbyFragment extends MvpFragment<LobbyContract.Controller, LobbyContract.Host> implements LobbyContract.View, View.OnClickListener {
    private LobbyContract.Controller controller;

    public LobbyFragment() {
        controller = new LobbyFragmentLocator().createController();
    }

    public static LobbyFragment newInstance() {
        return new LobbyFragment();
    }

    @Override
    protected LobbyContract.Controller getController() {
        return controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lobby, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.edit_profile).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (R.id.edit_profile == id && hasCallBack()) {
            getCallBack().showProfileScreen();
        }
    }
}
