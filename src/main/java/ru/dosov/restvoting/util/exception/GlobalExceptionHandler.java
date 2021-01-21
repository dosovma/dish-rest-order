package ru.dosov.restvoting.util.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.dosov.restvoting.util.ValidationUtil;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorAttributes errorAttributes;
    private final MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public GlobalExceptionHandler(ErrorAttributes errorAttributes, MessageSourceAccessor messageSourceAccessor) {
        this.errorAttributes = errorAttributes;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> appException(AppException ex, WebRequest request) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ex.getOptions());
        HttpStatus status = ex.getStatus();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        return ResponseEntity.status(status).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ValidationUtil.getRootCause(ex);
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", messageSourceAccessor.getMessage("error.appError"));
        return ResponseEntity.status(status).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleBindException(ex, headers, status, request);
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<Map<String, Object>> otherEx(Exception ex, WebRequest request) {
        Throwable e = ValidationUtil.getRootCause(ex);
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ValidationUtil.getMessage(e));
        return ResponseEntity.status(status).body(body);
    }
}
