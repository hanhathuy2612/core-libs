package com.hnh.enterprise.core.cqrs.query;

import com.hnh.enterprise.core.cqrs.Bus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryBus extends Bus<QueryHandler<?, ?>> {
    public QueryBus(List<QueryHandler<?, ?>> discovered) {
        super(discovered);
    }
}
