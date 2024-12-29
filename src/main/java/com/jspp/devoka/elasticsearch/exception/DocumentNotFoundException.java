package com.jspp.devoka.elasticsearch.exception;

import com.jspp.devoka.common.exception.BusinessException;
import com.jspp.devoka.common.exception.ErrorCode;

public class DocumentNotFoundException extends BusinessException {
    public DocumentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
