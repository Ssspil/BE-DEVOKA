package com.jspp.devoka.term.exception;

import com.jspp.devoka.common.exception.BusinessException;
import com.jspp.devoka.common.exception.ErrorCode;

public class TermNotFoundException extends BusinessException {

    public TermNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
