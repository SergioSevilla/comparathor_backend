package com.comparathor.backend.exception;

public class UserDuplicatedException extends RuntimeException {
    public UserDuplicatedException(String message){
        super(message);
    }
}
