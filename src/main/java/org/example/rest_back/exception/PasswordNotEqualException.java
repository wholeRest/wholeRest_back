package org.example.rest_back.exception;

public class PasswordNotEqualException extends RuntimeException{
    public PasswordNotEqualException(String message){super(message);}
}
