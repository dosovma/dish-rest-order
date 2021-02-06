package ru.dosov.restvoting.util.exceptionhandler.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.dosov.restvoting.util.ValidationUtil;
import ru.dosov.restvoting.util.exceptionhandler.exception.AppException;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorAttributes errorAttributes;
    private final MessageSourceAccessor messageSourceAccessor;

    private static final Map<String, String> CONSTRAIN_MAP = Map.of(
            "users_email_idx", "exception.user.duplicateEmail",
            "date_rest_idx", "exception.restaurant.duplicateMenuDateTime",
            "date_user_idx", "exception.vote.duplicateDateTime",
            "restaurant_idx", "exception.restaurant.duplicateName",
            "dish_idx", "exception.dish.duplicateName"
    );

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
        log.warn("{} at request  {}: {}", status.getReasonPhrase(), request.getDescription(false), ex.toString());
        return ResponseEntity.status(status).body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", ValidationUtil.buildErrorMessage(ex, messageSourceAccessor));
        log.warn("{} at request  {}: {}", status.getReasonPhrase(), request.getDescription(false), ex.toString());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> conflict(DataIntegrityViolationException e, WebRequest request) {
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        String messageError = ValidationUtil.getRootCause(e).getMessage();
        if (messageError != null) {
            String lowerCaseMsg = messageError.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAIN_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    messageError = messageSourceAccessor.getMessage(entry.getValue());
                }
            }
        }
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("error", HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
        body.put("message", messageError);
        log.warn("{} at request  {}: {}", HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), request.getDescription(false), e.toString());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<Map<String, Object>> accessDenied(Exception ex, WebRequest request) {
        Throwable e = ValidationUtil.getRootCause(ex);
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        HttpStatus status = HttpStatus.FORBIDDEN;
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", e.getMessage());
        log.warn("{} at request  {}: {}", status.getReasonPhrase(), request.getDescription(false), e.toString());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<Map<String, Object>> otherEx(Exception ex, WebRequest request) {
        Throwable e = ValidationUtil.getRootCause(ex);
        Map<String, Object> body = errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION));
        HttpStatus status = HttpStatus.BAD_REQUEST;
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", e.getMessage());
        log.error(status.getReasonPhrase() + " at request " + request.getDescription(false), e);
        return ResponseEntity.status(status).body(body);
    }
}
