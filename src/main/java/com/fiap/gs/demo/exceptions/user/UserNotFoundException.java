package com.fiap.gs.demo.exceptions.user;

import com.fiap.gs.demo.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
