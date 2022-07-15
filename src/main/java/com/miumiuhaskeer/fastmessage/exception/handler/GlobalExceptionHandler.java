package com.miumiuhaskeer.fastmessage.exception.handler;

import com.miumiuhaskeer.fastmessage.exception.ChatNotExistException;
import com.miumiuhaskeer.fastmessage.exception.FMSRequestException;
import com.miumiuhaskeer.fastmessage.exception.RefreshTokenExpiredException;
import com.miumiuhaskeer.fastmessage.exception.RegistrationFailedException;
import com.miumiuhaskeer.fastmessage.exception.UserAlreadyExistException;
import com.miumiuhaskeer.fastmessage.model.response.ResponseEntityBuilder;
import com.miumiuhaskeer.fastmessage.properties.bundle.ErrorBundle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseEntityBuilder.SimpleResponse> handleConstraintViolationException(ConstraintViolationException e) {
        List<ConstraintViolation<?>> exceptions = new ArrayList<>(e.getConstraintViolations());

        e.printStackTrace();

        return new ResponseEntityBuilder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exceptions.get(0).getMessage())
                .create();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseEntityBuilder.SimpleResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String message = ErrorBundle.get("error.methodArgumentNotValidException.message");

        e.printStackTrace();

        if (errors.size() > 0) {
            message = ErrorBundle.getFromText(errors.get(0).getDefaultMessage());
        }

        return new ResponseEntityBuilder()
                .status(HttpStatus.BAD_REQUEST)
                .message(message)
                .create();
    }

    @ExceptionHandler({
            UserAlreadyExistException.class,
            EntityNotFoundException.class,
            RefreshTokenExpiredException.class,
            RegistrationFailedException.class,
            AuthenticationException.class,
            UsernameNotFoundException.class,
            IllegalArgumentException.class,
            ChatNotExistException.class
    })
    public ResponseEntity<ResponseEntityBuilder.SimpleResponse> handleBadRequestException(Exception e) {
        e.printStackTrace();

        return new ResponseEntityBuilder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .create();
    }

    // TODO add reading of error messages
    @ExceptionHandler({
            RestClientException.class,
            FMSRequestException.class
    })
    public ResponseEntity<ResponseEntityBuilder.SimpleResponse> handleRestClientException(Exception e) {
        e.printStackTrace();

        HttpStatus status;
        String message;

        if (e instanceof HttpClientErrorException.Unauthorized) {
            status = HttpStatus.UNAUTHORIZED;
            message = ErrorBundle.get("error.notAuthenticatedException.message");
        } else if (e instanceof HttpClientErrorException.BadRequest) {
            status = HttpStatus.BAD_REQUEST;
            message = ErrorBundle.get("error.fmsRequestException.304.message");
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = ErrorBundle.get("error.fmsRequestException.500.message");
        }

        return new ResponseEntityBuilder()
                .status(status)
                .message(message)
                .create();
    }
}
