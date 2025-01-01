package com.jspp.devoka.elasticsearch.exception;

import com.jspp.devoka.common.exception.BusinessException;
import com.jspp.devoka.common.exception.ErrorCode;

public class IndexAlreadyExistException extends BusinessException {
    public IndexAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
