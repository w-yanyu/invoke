package cn.iyanyu.invoke.error.exception.support;

import cn.iyanyu.base.comm.error.BusinessException;
import cn.iyanyu.base.comm.error.ErrorCode;
import cn.iyanyu.base.comm.lang.IString;
import cn.iyanyu.base.comm.lang.Params;

public class AppOnlError {

    public static class InvokeError {
        public static final ErrorCode E0001_CODE = new ErrorCode("APP.Invoke.E0001", "E0001", "E0001");
        public static final ErrorCode E0002_CODE = new ErrorCode("APP.Invoke.E0002", "E0002", "E0002");
        public static final ErrorCode E0003_CODE = new ErrorCode("APP.Invoke.E0003", "E0003", "E0003");
        public static final ErrorCode E0004_CODE = new ErrorCode("APP.Invoke.E0004", "E0004", "E0004");
        public static final ErrorCode E0005_CODE = new ErrorCode("APP.Invoke.E0005", "E0005", "E0005");
        public static final ErrorCode E0006_CODE = new ErrorCode("APP.Invoke.E0006", "E0006", "E0006");

        public static BusinessException E0001(String converterType) {
            throw new BusinessException(E0001_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E0001", "不支持的包转换器类型[{converterType}]", new Params().add("converterType", converterType)).getString());
        }

        public static InvokeTimeOutException E0002(String errorMsg, Throwable t) {
            throw new InvokeTimeOutException(E0002_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E0002", "REST 调用因超时失败。错误信息为 [{errorMsg}]", new Params().add("errorMsg", errorMsg)).getString(), t);
        }

        public static InvokeException E0003(String clientName, String serviceName, String serviceCode, String errorMsg, Throwable t) {
            throw new InvokeException(E0003_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E0003", "{clientName}外调{serviceName}的{serviceCode}服务失败，原因：[{errorMsg}]", new Params().add("clientName", clientName).add("serviceName", serviceName).add("serviceCode", serviceCode).add("errorMsg", errorMsg)).getString(),t);
        }

        public static InvokeException E0004(String converterType) {
            throw new InvokeException(E0004_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E0004", "不支持的包转换器类型[${converterType}]", new Params().add("converterType", converterType)).getString());
        }

        public static InvokeException E0005() {
            throw new InvokeException(E0005_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E0005", "服务调用失败，消息队列（MQ）未启用").getString());
        }

        public static InvokeException E0006(String msg, Throwable t) {
            throw new InvokeException(E0006_CODE.getErrorCode(), new IString("AppOnlError.InvokeError.E0006", "MQ服务调用失败，错误信息为：${msg}", new Params().add("msg", msg)).getString(), t);
        }
    }
}
