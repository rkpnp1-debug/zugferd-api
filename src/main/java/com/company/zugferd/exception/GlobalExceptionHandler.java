package com.company.zugferd.exception;

import com.company.zugferd.dto.InvoiceResponse;
import com.company.zugferd.filter.CorrelationIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Sanitized error responses – never expose stack traces, paths, filenames or internal details (SO.SA-1.9).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvoiceResponse> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " is invalid")
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        log.warn("Validation failed. correlationId={}", MDC.get(CorrelationIdFilter.MDC_KEY));
        return ResponseEntity.badRequest()
                .body(InvoiceResponse.fail(null, "Request validation failed", details));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<InvoiceResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        log.warn("Unreadable request body. correlationId={}", MDC.get(CorrelationIdFilter.MDC_KEY));
        return ResponseEntity.badRequest()
                .body(InvoiceResponse.fail(null, "Invalid request body", "JSON could not be parsed"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<InvoiceResponse> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(InvoiceResponse.fail(null, "Unsupported media type", "Content-Type must be application/json"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<InvoiceResponse> handleMethod(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(InvoiceResponse.fail(null, "Method not allowed", null));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<InvoiceResponse> handleSecurity(SecurityException ex) {
        log.warn("Security violation. correlationId={}", MDC.get(CorrelationIdFilter.MDC_KEY));
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(InvoiceResponse.fail(null, "Forbidden", null));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<InvoiceResponse> handleNotFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(InvoiceResponse.fail(null, "Not found", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<InvoiceResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unhandled error. correlationId={} path={}", MDC.get(CorrelationIdFilter.MDC_KEY),
                request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(InvoiceResponse.fail(null, "Internal error", "An unexpected error occurred"));
    }
}
