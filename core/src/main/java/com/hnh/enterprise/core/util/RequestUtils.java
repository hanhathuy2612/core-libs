package com.hnh.enterprise.core.util;


import com.hnh.enterprise.core.rest.request.DirectionType;
import com.hnh.enterprise.core.rest.request.RequestInfoDTO;
import com.hnh.enterprise.core.rest.request.SortInfoDTO;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestUtils {
    public static final Integer PAGE_INDEX_DEFAULT = 0;
    public static final Integer PAGE_SIZE_DEFAULT = 50;
    public static final Integer PAGE_SIZE_MAX = 2147483647;
    
    public static Pageable getPageRequest(RequestInfoDTO requestInfo) {
        int page = requestInfo.getPage() < 0 ? PAGE_INDEX_DEFAULT : requestInfo.getPage();
        int size = requestInfo.getSize();
        
        if (size == -1) {
            size = PAGE_SIZE_MAX;
        } else if (size < 1) {
            size = PAGE_SIZE_DEFAULT;
        }
        
        PageRequest pageRequest = PageRequest.of(page, size);
        
        List<SortInfoDTO> sortInfoList = requestInfo.getSortInfo();
        if (!CollectionUtils.isEmpty(sortInfoList)) {
            List<Sort.Order> orders = sortInfoList.stream().map(sortInfo -> {
                String property = sortInfo.getField();
                if (sortInfo.getDirection() == DirectionType.UNSORTED) {
                    return Sort.Order.by(property).nullsLast();
                }
                Sort.Direction direction = sortInfo.getDirection() == DirectionType.DESCENDING ? Sort.Direction.DESC : Sort.Direction.ASC;
                return new Sort.Order(direction, property).nullsLast();
            }).collect(Collectors.toList());
            pageRequest = pageRequest.withSort(Sort.by(orders));
        }
        
        return pageRequest;
    }
}
