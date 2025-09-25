package com.hnh.enterprise.core.cqrs.command;

import com.hnh.enterprise.core.cqrs.Bus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandBus extends Bus<CommandHandler<?, ?>> {
    public CommandBus(List<CommandHandler<?, ?>> discovered) {
        super(discovered);
    }
}
