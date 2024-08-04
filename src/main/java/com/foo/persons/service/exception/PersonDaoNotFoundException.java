package com.foo.persons.service.exception;

public class PersonDaoNotFoundException extends PersonDaoException{
    public PersonDaoNotFoundException(String message) {
        super(message);
    }
}
