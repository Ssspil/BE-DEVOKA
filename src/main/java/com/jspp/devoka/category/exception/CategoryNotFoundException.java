package com.jspp.devoka.category.exception;

import com.jspp.devoka.common.exception.BusinessException;
import com.jspp.devoka.common.exception.ErrorCode;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}