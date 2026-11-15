package cn.iyanyu.invoke.error.exception.support;

import cn.iyanyu.base.error.BusinessException;
import cn.iyanyu.base.error.ErrorCode;
import cn.iyanyu.base.lang.IString;
import cn.iyanyu.base.lang.Params;

public class AppOnlError {

    public static class InvokeError {
        public static final ErrorCode E0001_CODE = new ErrorCode("APP.Invoke.E001", "E001", "E0001");

        public static BusinessException EOO1(String converterType) {
            throw new BusinessException(E0001_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E001","不支持的包转换器类型[{converterType}]", new Params().add("converterType",converterType)).getString());
        }
    }
}
