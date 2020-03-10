package com.mdgd.academy2020.models.validators;

public interface Validator<T> {
    String validate(T data);
}
