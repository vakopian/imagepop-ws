package com.imagepop.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by BernardXie on 3/21/16.
 */

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Validation Error")
public final class ValidationErrorException extends RuntimeException{
    public ValidationErrorException() {
        super();
    }

    public ValidationErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ValidationErrorException(final String message) {
        super(message);
    }

    public ValidationErrorException(final Throwable cause) {
        super(cause);
    }
}
