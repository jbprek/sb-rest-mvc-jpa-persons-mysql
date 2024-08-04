package com.foo.app.service.exception;

public class PersonDaoExistsException extends PersonDaoException{
    public PersonDaoExistsException(String message) {
        super(message);
    }

    public PersonDaoExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
