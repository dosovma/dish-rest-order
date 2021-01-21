package ru.dosov.restvoting.util.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends AppException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}