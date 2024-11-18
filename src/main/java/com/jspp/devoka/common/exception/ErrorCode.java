package com.jspp.devoka.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    /***************************************
     * 공통 에러 처리
     ***************************************/
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "C1000", "서비스 오류가 발생하였습니다."),
    INVALID_INPUT_VALUE(BAD_REQUEST, "C1001", "잘못된 입력이 있습니다."),
    HTTP_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "C1002", "허용하지 않는 HTTP 메서드입니다."),

    /***************************************
     * 카테고리 에러 처리
     ***************************************/
    NOT_FOUND_CATEGORY(NOT_FOUND, "C2000", "존재하지 않는 카테고리 번호입니다."),

    /***************************************
     * 용어 에러 처리
     ***************************************/
    NOT_FOUND_TERM(NOT_FOUND, "T2000", "존재하지 않는 용어 번호입니다.");




    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
