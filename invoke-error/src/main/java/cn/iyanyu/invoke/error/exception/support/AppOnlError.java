package cn.iyanyu.invoke.error.exception.support;

import cn.iyanyu.base.error.BusinessException;
import cn.iyanyu.base.error.ErrorCode;
import cn.iyanyu.base.lang.IString;
import cn.iyanyu.base.lang.Params;

public class AppOnlError {

    public static class InvokeError {
        public static final ErrorCode E0001_CODE = new ErrorCode("APP.Invoke.E001", "E001", "E0001");
        public static final ErrorCode E0002_CODE = new ErrorCode("APP.Invoke.E002", "E002", "E0002");
        public static final ErrorCode E0003_CODE = new ErrorCode("APP.Invoke.E003", "E003", "E0003");

        public static BusinessException E0001(String converterType) {
            throw new BusinessException(E0001_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E001", "不支持的包转换器类型[{converterType}]", new Params().add("converterType", converterType)).getString());
        }

        public static InvokeTimeOutException E0002(String errorMsg, Throwable t) {
            throw new InvokeTimeOutException(E0002_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E002", "REST 调用因超时失败。错误信息为 [{errorMsg}]", new Params().add("errorMsg", errorMsg)).getString(), t);
        }

        public static BusinessException E0003(String clientName, String serviceName, String serviceCode, String errorMsg) {
            throw new BusinessException(E0003_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E003", "{clientName}外调{serviceName}的{serviceCode}服务失败，原因：[{errorMsg}]", new Params().add("clientName", clientName).add("serviceName", serviceName).add("serviceCode", serviceCode).add("errorMsg", errorMsg)).getString());
        }
    }
}
