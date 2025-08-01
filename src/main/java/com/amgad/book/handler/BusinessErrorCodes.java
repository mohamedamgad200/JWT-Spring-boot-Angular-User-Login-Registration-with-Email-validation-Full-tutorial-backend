package com.amgad.book.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),

    INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST, "Current password is incorrect"),

    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "New password does not match"),

    ACCOUNT_LOCKED(302, FORBIDDEN, "Account is locked"),

    ACCOUNT_DISABLED(303, FORBIDDEN, "Account is disabled"),

    BAD_CREDENTIALS(304, FORBIDDEN, "Bad credentials"),

    INTERNAL_SERVER(305, INTERNAL_SERVER_ERROR, "Internal server error please try again later"),

    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
