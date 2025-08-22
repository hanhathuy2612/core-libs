package com.hnh.enterprise.core.rest.mapper;

import com.hnh.enterprise.core.rest.response.PageInfo;
import com.hnh.enterprise.core.rest.response.PageResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Function;

@UtilityClass
public class CommonMapper {
    public <E, D> PageResponse<D> toPageResponse(Page<E> pageable, Function<E, D> converter) {
        List<D> dtoList = pageable.getContent().stream()
            .map(converter)
            .toList();
        
        PageInfo<D> pageInfo = PageInfo.<D>builder()
            .page(pageable.getNumber())
            .size(pageable.getSize())
            .total(pageable.getTotalPages())
            .content(dtoList)
            .build();
        
        return PageResponse.<D>builder()
            .status(HttpStatus.OK)
            .data(pageInfo)
            .build();
    }
} 
