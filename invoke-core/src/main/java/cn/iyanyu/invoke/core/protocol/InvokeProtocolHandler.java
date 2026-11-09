package cn.iyanyu.invoke.core.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InvokeProtocolHandler {
    private static final Map<String, InvokeProtocolHandler> CACHE = new HashMap<>();

    public InvokeProtocolHandler() {
    }

    public static void init() {
        List<InvokeProtocolHandler> protocolHandlers = FactoriesLoader.getAllFactories(InvokeProtocolHandler.class);
        Iterator var1 = protocolHandlers.iterator();

        while(var1.hasNext()) {
            ServiceInvokeProtocolHandler protocolHandler = (ServiceInvokeProtocolHandler)var1.next();
            CACHE.put(protocolHandler.getSpiId(), protocolHandler);
            logger.info("[Service invoke] Load service invoke protocol by SPI, protocol id=[{}], protocol class:[{}]", protocolHandler.getSpiId(), protocolHandler.getClass().getName());
        }

    }

    public static void put(String protocolType, ServiceInvokeProtocolHandler handler) {
        CACHE.put(protocolType, handler);
    }

    public static ServiceInvokeProtocolHandler get(String protocolType) {
        ServiceInvokeProtocolHandler handler = (ServiceInvokeProtocolHandler)CACHE.get(protocolType);
        if (handler == null) {
            throw ServiceInvoke.E1003(protocolType);
        } else {
            return handler;
        }
    }
}
