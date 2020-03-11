package com.mdgd.academy2020.models.validators;

import android.content.Context;

import com.google.common.base.Optional;
import com.mdgd.academy2020.R;
import com.mdgd.academy2020.util.TextUtil;

public class PasswordValidator implements Validator<String> {
    private final Context context;

    public PasswordValidator(Context context) {
        this.context = context;
    }

    @Override
    public Optional<String> validate(String data) {
        return Optional.fromNullable(!TextUtil.isEmpty(data) && data.length() >= 6 ? null : context.getString(R.string.password_too_short));
    }
}
