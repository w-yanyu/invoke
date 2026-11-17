package cn.iyanyu.invoke.api.protocol;

import cn.iyanyu.invoke.api.model.InvokeContext;
import cn.iyanyu.invoke.api.pkg.InvokePkgConverter;

public interface InvokeProtocolHandler {
    <RES> void invoke(InvokeContext<RES> invokeContext);

    InvokePkgConverter getPkgConverter(String converterType);

    String getSpiId();
}