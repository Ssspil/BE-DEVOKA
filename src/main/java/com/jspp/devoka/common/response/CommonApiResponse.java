package com.jspp.devoka.common.response;


import com.jspp.devoka.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@AllArgsConstructor
public class CommonApiResponse<T> {

    private ResponseHeader responseHeader;
    private T response;


    /**
     * 요청 성공 했을 때
     * @param data
     * @param dataType
     * @return
     * @param <T>
     */
    public static <T> CommonApiResponse<T> success(T data, DataType dataType){
        // 응답 헤더 생성
        ResponseHeader header = new ResponseHeader.ResponseHeaderBuilder()
                                            .resultCode("0")
                                            .message("성공")
                                            .dataType(dataType.name()).build();


        return new CommonApiResponse<>(header, data);
    }

    // 비즈니스 로직 예외처리
    public static <T> CommonApiResponse<T> failure(ErrorCode error){
        // 응답 헤더 생성
        ResponseHeader header = new ResponseHeader.ResponseHeaderBuilder()
                .resultCode(error.getCode())
                .message(error.getMessage())
                .dataType(null).build();

        return new CommonApiResponse<>(header, null);
    }

    // 유효성 체크할 때 메시지만 내려줄 때
    public static <T> CommonApiResponse<T> failure(String msg){
        // 응답 헤더 생성
        ResponseHeader header = new ResponseHeader.ResponseHeaderBuilder()
                .resultCode(ErrorCode.INVALID_INPUT_VALUE.getCode())
                .message(msg)
                .dataType(null).build();

        return new CommonApiResponse<>(header, null);
    }

    /**
     * 공통 API 응답 헤더 - 개발자들이 보는 용
     */
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ResponseHeader {
        private final String resultCode;
        private final String message;
        private final String dataType;
    }

}
