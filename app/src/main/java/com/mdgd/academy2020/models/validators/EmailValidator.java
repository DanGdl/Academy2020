package com.mdgd.academy2020.models.validators;

import android.content.Context;

import com.mdgd.academy2020.R;

import java.util.regex.Pattern;

public class EmailValidator implements Validator<String> {
    private final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final Context context;

    public EmailValidator(Context context) {
        this.context = context;
    }

    @Override
    public String validate(String data) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(data).matches() ? null : context.getString(R.string.email_is_not_valid);
    }
}
