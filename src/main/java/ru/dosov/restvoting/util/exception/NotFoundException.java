package ru.dosov.restvoting.util.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}