package com.intexsoft.controller;

import com.intexsoft.exception.DuplicateNameException;
import com.intexsoft.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@ControllerAdvice
public abstract class CommonController {

    private static final String TIME_ERROR = "Response time exceeded";

    private static final long TIMEOUT = 60000;

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<String> acceptDuplicateKeyException(DuplicateKeyException ex) {
        log.error("Duplicate key exception: ", ex);
        return status(BAD_REQUEST).body("Duplicate key exception: " + ex.getCause());
    }

    @ExceptionHandler(DuplicateNameException.class)
    ResponseEntity<String> acceptDuplicateNameException(DuplicateNameException ex) {
        log.error("Duplicate name exception: ", ex);
        return status(BAD_REQUEST).body("Duplicate key exception: " + ex.getCause());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<String> acceptDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation exception: ", ex);
        return status(BAD_REQUEST).body("Data integrity violation exception: " + ex.getCause());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> acceptIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument exception: ", ex);
        return status(BAD_REQUEST).body("Illegal argument exception: " + ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> acceptNotFoundException(NotFoundException ex) {
        log.error("Not found ", ex);
        return status(NOT_FOUND).body(ex.getMessage());
    }

    protected final <T> DeferredResult<T> getDeferredResult(long timeout) {
        DeferredResult<T> result = new DeferredResult<>(timeout);
        result.onTimeout(() -> result.setErrorResult(status(HttpStatus.INTERNAL_SERVER_ERROR).body(TIME_ERROR)));
        return result;
    }

    protected final <T> DeferredResult<T> getDeferredResult() {
        return getDeferredResult(TIMEOUT);
    }

    protected final <T> Observable.Transformer<T, T> convertToDeferredResult(DeferredResult<T> result) {
        return source -> source
                .take(1)
                .doOnNext(result::setResult)
                .doOnError(e -> {
                    log.error("Error in the Observable", result.toString(), e);
                    result.setErrorResult(e);
                })
                .doOnCompleted(() -> {
                    if (!result.isSetOrExpired()) {
                        result.setErrorResult(new NotFoundException("search object not found"));
                    }
                });
    }
}
