package com.hnh.enterprise.core.rest.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class PageResponse<T> extends BaseResponse<PageInfo<T>> {
}
