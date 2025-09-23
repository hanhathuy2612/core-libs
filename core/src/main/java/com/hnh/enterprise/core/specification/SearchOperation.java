package com.hnh.enterprise.core.specification;

/**
 * Enum of operations that can be used in search criteria when constructing
 * dynamic JPA specifications. These correspond to common comparison
 * operations used in query predicates.
 */
public enum SearchOperation {
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_EQUAL,
    LESS_THAN_EQUAL,
    NOT_EQUAL,
    EQUAL,
    MATCH,
    MATCH_START,
    MATCH_END,
    IN,
    NOT_IN
}