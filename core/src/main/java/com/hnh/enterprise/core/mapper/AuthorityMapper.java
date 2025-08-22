package com.hnh.enterprise.core.mapper;

import com.hnh.enterprise.core.entity.Authority;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    default Authority fromString(String name) {
        Authority a = new Authority();
        a.setName(name);
        return a;
    }
    
    default String toString(Authority authority) {
        return authority.getName();
    }
}
