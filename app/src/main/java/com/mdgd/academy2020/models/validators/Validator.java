package com.mdgd.academy2020.models.validators;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

public interface Validator<T> {
    @NonNull
    Optional<String> validate(T data);
}
