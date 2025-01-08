package com.jspp.devoka.term.exception;

import com.jspp.devoka.common.exception.BusinessException;
import com.jspp.devoka.common.exception.ErrorCode;

public class InvalidSearchKeyword extends BusinessException  {

    public InvalidSearchKeyword(ErrorCode errorCode) {
        super(errorCode);
    }
}