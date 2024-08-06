package com.foo.persons.rest;

import com.foo.persons.service.exception.PersonDaoException;
import com.foo.persons.service.exception.PersonDaoExistsException;
import com.foo.persons.service.exception.PersonDaoNotFoundException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;



@Slf4j
@ControllerAdvice
public class PersonApiRestErrorHandler {

    @ExceptionHandler(PersonDaoNotFoundException.class)
    public ModelAndView handlePersonDaoNotFoundException(PersonDaoNotFoundException ex, ServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, ex.getMessage());

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/error");
        return mav;
    }

    @ExceptionHandler(PersonDaoExistsException.class)
    public ModelAndView handlePersonDaoExistsException(PersonDaoExistsException ex, ServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.BAD_REQUEST.value());
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, ex.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/error");
        return mav;
    }

    @ExceptionHandler(PersonDaoException.class)
    public ModelAndView handlePersonDaoException(PersonDaoException ex, ServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.BAD_REQUEST.value());
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, ex.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/error");
        return mav;
    }


    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllUncaughtException(Exception ex, ServletRequest request) {
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, ex.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/error");
        return mav;
    }


}


