package com.mdgd.academy2020.arch.fragments;

import com.mdgd.academy2020.arch.Contract;

public abstract class MessageDialog<Presenter extends Contract.Controller, Host extends Contract.Host> extends MvpDialogFragment<Presenter, Host> {
    protected static final String KEY_TITLE = "key_title";
    protected static final String KEY_MSG = "key_msg";
    protected static final String KEY_TITLE_STR = "key_title_str";
    protected static final String KEY_MSG_STR = "key_msg_str";
    protected static final String KEY_TYPE = "key_type";
    protected static final int TYPE_INT = 1;
    protected static final int TYPE_STR = 2;

}
