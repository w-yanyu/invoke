package cn.iyanyu.invoke.api.invoker;

import cn.iyanyu.invoke.api.model.InvokeContext;

public interface ServiceInvoker {
    <RES> void invoke(InvokeContext<RES> invokeContext);
}