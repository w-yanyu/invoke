package cn.iyanyu.invoke.error.exception.support;

import cn.iyanyu.base.error.SystemException;

public class InvokeTimeOutException extends SystemException {

    public InvokeTimeOutException(String code, String message) {
        super(code, message);
    }

    public InvokeTimeOutException(String code, String message, String... params) {
        super(code, message, params);
    }

    public InvokeTimeOutException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
