package cn.iyanyu.invoke.core.util;

public class ServiceInvokeRequestUtils {
    public ServiceInvokeRequestUtils() {
    }

    public static OnlineRequest getOnlineRequestFromRequest(Map<String, Object> requestParam) {
        if (requestParam != null && requestParam.size() == 1) {
            Object body = requestParam.values().iterator().next();
            return !body.getClass().isAssignableFrom(OnlineRequest.class) ? null : (OnlineRequest)body;
        } else {
            return null;
        }
    }
}