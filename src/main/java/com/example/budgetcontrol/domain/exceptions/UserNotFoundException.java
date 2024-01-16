package com.example.budgetcontrol.domain.exceptions;

import com.example.budgetcontrol.domain.FieldNames;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String keyName, String keyValue) {
        super(FieldNames.USER, keyName, keyValue);
    }
}
