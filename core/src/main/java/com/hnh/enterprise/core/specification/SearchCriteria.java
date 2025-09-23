package com.hnh.enterprise.core.specification;

/**
 * Represents a single condition in a dynamic query specification. Each
 * criteria consist of a field name (key), a {@link SearchOperation}
 * indicating how the field should be compared, and the value used for
 * comparison.
 */
public record SearchCriteria(String key, SearchOperation operation, Object value) {
}