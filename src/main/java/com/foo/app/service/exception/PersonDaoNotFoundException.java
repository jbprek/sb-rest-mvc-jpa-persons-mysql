package com.foo.app.service.exception;

public class PersonDaoNotFoundException extends PersonDaoException{
    public PersonDaoNotFoundException(String message) {
        super(message);
    }
}
