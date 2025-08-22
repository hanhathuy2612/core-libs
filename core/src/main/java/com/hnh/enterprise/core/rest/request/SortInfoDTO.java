package com.hnh.enterprise.core.rest.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SortInfoDTO {
    String field;
    DirectionType direction;
}
