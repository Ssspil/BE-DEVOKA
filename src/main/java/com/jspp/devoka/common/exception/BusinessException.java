package com.jspp.devoka.common.exception;


import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
