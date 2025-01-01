package com.jspp.devoka.elasticsearch.exception;

import com.jspp.devoka.common.exception.BusinessException;
import com.jspp.devoka.common.exception.ErrorCode;

public class IndexNotFoundException extends BusinessException  {

    public IndexNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
