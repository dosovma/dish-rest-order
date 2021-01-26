package ru.dosov.restvoting.util.exceptionhandler.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AppException extends ResponseStatusException {
    private final ErrorAttributeOptions options;
    private final String message;


    public AppException(HttpStatus status, String message, ErrorAttributeOptions options) {
        super(status, message);
        this.options = options;
        this.message = message;
    }

    public ErrorAttributeOptions getOptions() {
        return options;
    }

        public String getAppExMessage() {
        return message;
    }
}
