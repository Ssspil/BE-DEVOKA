package com.jspp.devoka.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    /***************************************
     * 공통 에러 처리
     ***************************************/
    INVALID_INPUT_VALUE(BAD_REQUEST, "C1000", "잘못된 입력이 있습니다."),
    HTTP_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "C1001", "허용하지 않는 HTTP 메서드입니다.");




    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
