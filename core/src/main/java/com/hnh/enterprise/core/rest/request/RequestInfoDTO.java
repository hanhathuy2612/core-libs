package com.hnh.enterprise.core.rest.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RequestInfoDTO {
    Integer page;
    Integer size;
    @Builder.Default
    List<SortInfoDTO> sortInfo = new ArrayList<>();
}
