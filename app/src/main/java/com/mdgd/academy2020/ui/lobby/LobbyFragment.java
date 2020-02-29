package com.mdgd.academy2020.ui.lobby;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

public class LobbyFragment extends MvpFragment<LobbyContract.Controller, LobbyContract.Host> implements LobbyContract.View {
    private LobbyContract.Controller controller;

    public static LobbyFragment newInstance() {
        return new LobbyFragment();
    }

    public LobbyFragment() {
        controller = new LobbyFragmentLocator().createController();
    }

    @Override
    protected LobbyContract.Controller getController() {
        return controller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
