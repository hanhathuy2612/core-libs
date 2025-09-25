package com.hnh.enterprise.core.cqrs.command;

import com.hnh.enterprise.core.cqrs.Handler;

public interface CommandHandler<C extends Command, R> extends Handler<C, R> {
    R handle(C command);
}
