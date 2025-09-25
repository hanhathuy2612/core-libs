package com.hnh.enterprise.core.cqrs;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Bus<H extends Handler<?, ?>> {
    private final Map<Class<?>, H> handlers = new HashMap<>();

    public Bus(List<H> discovered) {
        for (H h : discovered) {
            Class<?> type = resolveQueryType(h);
            if (type == null) {
                throw new IllegalStateException("Cannot resolve query type for: " + h.getClass());
            }
            if (handlers.putIfAbsent(type, h) != null) {
                throw new IllegalStateException("Duplicate handler for query: " + type.getName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <A extends Action, R> R execute(A action) {
        Handler<A, R> h = (Handler<A, R>) handlers.get(action.getClass());
        if (h == null) throw new IllegalStateException("No handler for " + action.getClass().getName());
        return h.handle(action);
    }

    private Class<?> resolveQueryType(H handler) {
        Class<?> target = AopUtils.getTargetClass(handler);
        // Try direct interface
        ResolvableType as = ResolvableType.forClass(target).as(Handler.class);
        if (as.hasGenerics()) return as.getGeneric(0).resolve(); // the Q in QueryHandler<Q,R>

        // Fallback: search interfaces
        for (ResolvableType interfaceType : ResolvableType.forClass(target).getInterfaces()) {
            ResolvableType qh = interfaceType.as(Handler.class);
            if (qh.resolve() == Handler.class && qh.hasGenerics()) {
                return qh.getGeneric(0).resolve();
            }
        }
        return null;
    }
}
