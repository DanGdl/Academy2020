package com.mdgd.academy2020.arch.support.auth;

import com.google.common.base.Optional;

public final class PasswordValidationResult {
    public final String password;
    public final Optional<String> errorMessage;

    PasswordValidationResult(String password, Optional<String> errorMessage) {
        this.password = password;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() {
        return !errorMessage.isPresent();
    }
}
