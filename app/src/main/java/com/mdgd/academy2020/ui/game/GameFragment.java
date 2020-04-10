package com.mdgd.academy2020.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MvpFragment;

public class GameFragment extends MvpFragment<GameContract.Controller, GameContract.Host> implements GameContract.View {
    private GameContract.Controller controller;

    public GameFragment() {
        controller = new GameFragmentLocator().createController();
    }

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    protected GameContract.Controller getController() {
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
