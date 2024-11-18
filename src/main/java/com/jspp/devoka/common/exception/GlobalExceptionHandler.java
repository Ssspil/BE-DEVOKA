package com.jspp.devoka.common.exception;

import com.jspp.devoka.category.exception.CategoryNotFoundException;
import com.jspp.devoka.common.response.CommonApiResponse;
import com.jspp.devoka.term.exception.TermNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /********************************
     *  카테고리 예외
     ********************************/
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<CommonApiResponse<Void>> categoryNotFoundException(CategoryNotFoundException e){
        log.error("카테고리 예외 발생 : [{}]  {}", e.getErrorCode().getStatus(), e.getErrorCode().getMessage());
        CommonApiResponse<Void> response = CommonApiResponse.failure(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    /********************************
     *  용어 예외
     ********************************/
    @ExceptionHandler(TermNotFoundException.class)
    public ResponseEntity<CommonApiResponse<Void>> termNotFoundException(TermNotFoundException e) {
        log.error("용어 조회 예외 발생 : [{}]  {}", e.getErrorCode().getStatus(), e.getErrorCode().getMessage());
        CommonApiResponse<Void> response = CommonApiResponse.failure(e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }



    /********************************
     *  공통 전체 예외
     ********************************/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonApiResponse<Void>> methodArgumentNotValidException(MethodArgumentNotValidException e){
        String errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("데이터 유효성 검증 예외 발생 : [{}] 필드 : {} , 메시지 : {}", e.getStatusCode(), e.getBindingResult().getFieldError().getField(), errorMsg);
        CommonApiResponse<Void> response = CommonApiResponse.failure(errorMsg);
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonApiResponse<Void>> allException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        CommonApiResponse<Void> response = CommonApiResponse.failure(ErrorCode.SERVER_ERROR.getMessage());
        return ResponseEntity.status(ErrorCode.SERVER_ERROR.getStatus()).body(response);
    }
}
