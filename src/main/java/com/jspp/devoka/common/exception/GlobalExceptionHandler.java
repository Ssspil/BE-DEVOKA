package com.jspp.devoka.common.exception;

import com.jspp.devoka.term.exception.TermException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TermException.class)
    public ResponseEntity<Object> testTermException(TermException e) {
        log.error("TermException", e);
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> testTermException(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)  // 상태 코드 추가
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> testTermException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)  // 상태 코드 추가
                .body(e.getMessage());
    }
}
