package com.yd.core.utils;

public class BusinessException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -7617925338905953846L;

    /**
     * errCode:错误码
     *
     * @since Ver 1.1
     */
    private String code;
    /**
     * errParams:错误参数
     *
     * @since Ver 1.1
     */
    private Object[] errParams;

    /**
     * getCode(取错误码)
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    public String getErrorCode() {
        return code;
    }

    /**
     * getErrParams(取错误参数)
     *
     * @param name
     * @param @return 设定文件
     * @return Object[]    错误参数数组
     * @Exception 异常对象
     * @since CodingExample　Ver(编码范例查看) 1.1
     */
    public Object[] getErrParams() {
        return errParams;
    }

    public BusinessException(String code) {
        super();
        this.code = code;
    }

    public BusinessException(String code, Object[] errParams) {
        super();
        this.code = code;
        this.errParams = errParams;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, Object[] errParams, String message) {
        super(message);
        this.code = code;
        this.errParams = errParams;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String code, Object[] errParams, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.errParams = errParams;
    }

    public BusinessException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BusinessException(String code, Object[] errParams, Throwable cause) {
        super(cause);
        this.code = code;
        this.errParams = errParams;
    }

    public static BusinessException build(String code, String message) {
        return new BusinessException(code, message);
    }

}