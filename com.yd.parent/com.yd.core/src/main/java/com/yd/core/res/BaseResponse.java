package com.yd.core.res;


import java.io.Serializable;


public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_SUCCESS_CODE = "00";
    private String code = "unknow";
    private String message = "未知异常";
    private T result;

    public void setValue(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> BaseResponse<T> fail(String code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
    
    public static <T> BaseResponse<T> success(T data, String code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setResult(data);
        response.setMessage(message);
        return response;
    }

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.success(data, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> BaseResponse<T> success(T data, String message) {
        return BaseResponse.success(data, DEFAULT_SUCCESS_CODE, message);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
