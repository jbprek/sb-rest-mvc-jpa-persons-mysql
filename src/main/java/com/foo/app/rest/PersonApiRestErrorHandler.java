package com.foo.app.rest;

import com.foo.app.service.exception.PersonDaoException;
import com.foo.app.service.exception.PersonDaoExistsException;
import com.foo.app.service.exception.PersonDaoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class PersonApiRestErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        log.warn("ConstraintViolationException:", e);

        Function<ConstraintViolation, ErrorDto.ValidationError> constraintViolationMapper = v -> {
            var arr = v.getPropertyPath().toString().split("\\.");
            var node = arr.length == 0 ? "" : arr[arr.length - 1];
            return new ErrorDto.ValidationError(node, v.getMessage());
        };
        var validationErrors = e.getConstraintViolations().stream()
                .map(constraintViolationMapper)
                .collect(Collectors.toList());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid URL parameters", request, validationErrors);
    }

    /** Request Parameters Type mismatch */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        log.warn("MethodArgumentTypeMismatchException:", e);

        var validationErrors = List.of(new ErrorDto.ValidationError(e.getName(), "Invalid type"));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid URL parameter, type  name:"+e.getName(), request, validationErrors);
    }

    /** Request Parameters Validation errors */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(v -> new ErrorDto.ValidationError(v.getField(), v.getDefaultMessage()))
                .collect(Collectors.toList());

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid payload", request, fieldErrors);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error("handleExceptionInternal - Internal error", ex);
        if (statusCode.is5xxServerError()) {
            request.setAttribute("javax.servlet.error.exception", ex, 0);
        }

        return new ResponseEntity(body, headers, statusCode);
    }

    @ExceptionHandler(PersonDaoNotFoundException.class)
    public ResponseEntity<Object> handlePersonDaoNotFoundException(PersonDaoNotFoundException itemNotFoundException, WebRequest request) {
        log.warn("Failed to find the requested element", itemNotFoundException);
        return buildErrorResponse(itemNotFoundException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(PersonDaoExistsException.class)
    public ResponseEntity<Object> handlePersonDaoExistsException(PersonDaoExistsException itemNotFoundException, WebRequest request) {
        log.warn("Element already exists", itemNotFoundException);
        return buildErrorResponse(itemNotFoundException, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(PersonDaoException.class)
    public ResponseEntity<Object> handlePersonDaoException(PersonDaoException itemNotFoundException, WebRequest request) {
        log.error("DB Error", itemNotFoundException);
        return buildErrorResponse(itemNotFoundException, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex, WebRequest request) {
        log.error("Unknown error occurred", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            HttpStatus httpStatus, String message,
            WebRequest request,
            List<ErrorDto.ValidationError> errors) {
        var customErrorResponse =
                new ErrorDto(Instant.now(), httpStatus.value(), message, getPath(request),
                        getMethod(request), errors);

        return ResponseEntity.status(httpStatus).body(customErrorResponse);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      HttpStatus httpStatus,
                                                      WebRequest request) {
        return buildErrorResponse(httpStatus, exception.getMessage(), request, Collections.emptyList());
    }

    private static String getPath(WebRequest request) {
        var url = ((ServletWebRequest) request).getRequest().getRequestURI();
        return url.substring(url.indexOf('/'));
    }

    private static String getMethod(WebRequest request) {
        return ((ServletWebRequest) request).getHttpMethod().name();
    }

}


