package com.jspp.devoka.term.exception;

import com.jspp.devoka.common.exception.BusinessException;
import com.jspp.devoka.common.exception.ErrorCode;

public class TermException extends BusinessException {

    public TermException(ErrorCode errorCode) {
        super(errorCode);
    }
}
