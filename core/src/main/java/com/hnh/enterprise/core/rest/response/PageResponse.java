package com.hnh.enterprise.core.rest.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class PageResponse<T> extends BaseResponse<PageInfo<T>> {

    /**
     * Create paginated response from Spring Data Page
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        PageInfo<T> pageInfo = PageInfo.<T>builder()
                .content(page.getContent())
                .total((int) page.getTotalElements())
                .page(page.getNumber())
                .size(page.getSize())
                .build();

        return PageResponse.<T>builder()
                .data(pageInfo)
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }
}
