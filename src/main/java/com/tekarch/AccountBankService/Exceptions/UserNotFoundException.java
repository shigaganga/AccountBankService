package com.tekarch.AccountBankService.Exceptions;

public class UserNotFoundException extends RuntimeException {

    // Constructor to pass the message for the exception
    public UserNotFoundException(String message) {
        super(message);  // Pass the message to the parent RuntimeException class
    }
}
