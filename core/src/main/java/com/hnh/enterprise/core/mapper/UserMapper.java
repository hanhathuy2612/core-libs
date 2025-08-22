package com.hnh.enterprise.core.mapper;


import com.hnh.enterprise.core.dto.UserDTO;
import com.hnh.enterprise.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = AuthorityMapper.class)
public interface UserMapper extends EntityMapper<UserDTO, User> {
    UserDTO toDto(User user);
}
