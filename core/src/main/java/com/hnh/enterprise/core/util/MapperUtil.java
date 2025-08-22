package com.hnh.enterprise.core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@UtilityClass
public class MapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String convertToString(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (IOException e) {
            log.error("[JSON] payload: {} \n error: {}", payload, e.getMessage());
            return StringUtils.EMPTY;
        }
    }

    public static <T> Optional<T> convertToObj(String content, Class<T> clazz) {
        try {
            return Optional.of(objectMapper.readValue(content, clazz));
        } catch (Exception e) {
            log.error("[JSON] content: {} \n error: {}", content, e.getMessage());
            return Optional.empty();
        }
    }

}
