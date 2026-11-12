package cn.iyanyu.invoke.core.protocol;

import cn.iyanyu.invoke.api.factories.Factories;
import cn.iyanyu.invoke.api.protocol.InvokeProtocolHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvokeProtocolHandlerManager {
    private static final Map<String, InvokeProtocolHandler> CACHE = new ConcurrentHashMap<>();

    public InvokeProtocolHandlerManager() {
    }

    public static void init() {
        List<InvokeProtocolHandler> protocolHandlers = Factories.get().getAllFactories(InvokeProtocolHandler.class);
        for (InvokeProtocolHandler protocolHandler : protocolHandlers) {
            CACHE.put(protocolHandler.getSpiId(), protocolHandler);
        }
    }

    public static void put(String protocolType, InvokeProtocolHandler handler) {
        CACHE.put(protocolType, handler);
    }

    public static InvokeProtocolHandler get(String protocolType) {
        InvokeProtocolHandler handler = CACHE.get(protocolType);
        if (handler == null) {
            throw new IllegalStateException("not found protocolType: " + protocolType);
        } else {
            return handler;
        }
    }
}
