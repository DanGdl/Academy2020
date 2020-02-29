package com.mdgd.academy2020.ui.error;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MessageDialog;

public class ErrorFragment extends MessageDialog<ErrorContract.Controller, ErrorContract.Host> implements ErrorContract.View {

    public static ErrorFragment newInstance(String title, String message) {
        final Bundle b = new Bundle();
        b.putString(KEY_TITLE_STR, title);
        b.putString(KEY_MSG_STR, message);
        b.putInt(KEY_TYPE, TYPE_STR);
        return new ErrorFragment();
    }

    @Override
    protected ErrorContract.Controller getController() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            final int type = args.getInt(KEY_TYPE);
            // if (TYPE_INT == type) {
            //     dialog.setTitle(getString(args.getInt(KEY_TITLE, R.string.empty)));
            //     dialog.setMessage(getString(args.getInt(KEY_MSG, R.string.empty)));
            // } else if (TYPE_STR == type) {
            //     dialog.setTitle(args.getString(KEY_TITLE_STR, ""));
            //     dialog.setMessage(args.getString(KEY_MSG_STR, ""));
            // }
        }
    }
}
