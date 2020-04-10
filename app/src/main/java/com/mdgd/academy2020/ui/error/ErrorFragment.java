package com.mdgd.academy2020.ui.error;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.arch.fragments.MessageDialog;

public class ErrorFragment extends MessageDialog<ErrorContract.Controller, ErrorContract.Host>
        implements ErrorContract.View, DialogInterface.OnClickListener {

    public static ErrorFragment newInstance(String title, String message) {
        final Bundle b = new Bundle();
        b.putString(KEY_TITLE_STR, title);
        b.putString(KEY_MSG_STR, message);
        b.putInt(KEY_TYPE, TYPE_STR);
        final ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setArguments(b);
        return errorFragment;
    }

    @Override
    protected ErrorContract.Controller getController() {
        return null;
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        return inflater.inflate(R.layout.fragment_error, container, false);
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Bundle args = getArguments();
        if (args != null) {
            final int type = args.getInt(KEY_TYPE);
            if (TYPE_INT == type) {
                builder.setTitle(getString(args.getInt(KEY_TITLE, R.string.empty)));
                builder.setMessage(getString(args.getInt(KEY_MSG, R.string.empty)));
            } else if (TYPE_STR == type) {
                builder.setTitle(args.getString(KEY_TITLE_STR, ""));
                builder.setMessage(args.getString(KEY_MSG_STR, ""));
            }
        }
        builder.setPositiveButton(R.string.ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE: {
                dismissAllowingStateLoss();
            }
            break;
        }
    }
}
