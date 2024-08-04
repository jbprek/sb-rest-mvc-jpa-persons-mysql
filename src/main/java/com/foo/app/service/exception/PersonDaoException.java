package com.foo.app.service.exception;

public class PersonDaoException extends RuntimeException{
    public PersonDaoException(String message) {
        super(message);
    }

    public PersonDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
