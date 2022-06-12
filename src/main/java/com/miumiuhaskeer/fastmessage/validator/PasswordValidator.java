package com.miumiuhaskeer.fastmessage.validator;

import com.miumiuhaskeer.fastmessage.annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String PASSWORD_PATTERN = "[a-zA-Z0-9!\"#$%&'()*+,\\-./:;<=>?@\\[\\]^_`{|}~ ]*";
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 120;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
        //return value != null && validate(value) && isSuitable(value);
    }

    private boolean validate(String userPassword){
        return userPassword.matches(PASSWORD_PATTERN);
    }

    private boolean isSuitable(String userPassword){
        int length = userPassword.length();

        return MIN_LENGTH <= length && length <= MAX_LENGTH;
    }
}
