package com.hnh.enterprise.core.cqrs.query;

import com.hnh.enterprise.core.cqrs.Handler;

public interface QueryHandler<C extends Query, R> extends Handler<C, R> {
    R handle(C query);
}
