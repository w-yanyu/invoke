package cn.iyanyu.invoke.api.protocol;

import cn.iyanyu.invoke.api.model.InvokeContext;

public interface InvokeProtocolHandler {
    <RES> void invoke(InvokeContext<RES> invokeContext);
}