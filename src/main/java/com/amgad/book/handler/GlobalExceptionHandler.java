package com.amgad.book.handler;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse>handelException(LockedException lockedException){
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(BusinessErrorCodes.ACCOUNT_LOCKED.getDescription())
                        .errorMessage(lockedException.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse>handelException(DisabledException disabledException){
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(BusinessErrorCodes.ACCOUNT_DISABLED.getDescription())
                        .errorMessage(disabledException.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse>handelException(BadCredentialsException badCredentialsException){
        return ResponseEntity.status(UNAUTHORIZED).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                        .errorMessage(BusinessErrorCodes.BAD_CREDENTIALS.getDescription())
                        .build()
        );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse>handelException(MessagingException messagingException){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.INTERNAL_SERVER.getCode())
                        .businessErrorDescription(BusinessErrorCodes.INTERNAL_SERVER.getDescription())
                        .errorMessage(BusinessErrorCodes.INTERNAL_SERVER.getDescription())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse>handelException(MethodArgumentNotValidException methodArgumentNotValidException){
        Set<String> errors=new HashSet<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error->{
            String message=error.getDefaultMessage();
            errors.add(message);
        });
        return ResponseEntity.status(BAD_REQUEST).body(
                ExceptionResponse.builder()
                        .validationErrors(errors)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse>handelException(Exception exception){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(
                ExceptionResponse.builder()
                        .businessErrorCode(BusinessErrorCodes.INTERNAL_SERVER.getCode())
                        .businessErrorDescription(BusinessErrorCodes.INTERNAL_SERVER.getDescription())
                        .errorMessage(BusinessErrorCodes.INTERNAL_SERVER.getDescription())
                        .build()
        );
    }



}
