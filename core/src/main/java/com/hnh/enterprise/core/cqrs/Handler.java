package com.hnh.enterprise.core.cqrs;

public interface Handler<A extends Action, R> {
    R handle(A action);
}
