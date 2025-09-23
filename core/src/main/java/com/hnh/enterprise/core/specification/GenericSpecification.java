package com.hnh.enterprise.core.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

/**
 * Generic implementation of Spring Data JPA {@link Specification} that
 * constructs a predicate based on a single {@link SearchCriteria}. It
 * supports a variety of comparison and matching operations defined in
 * {@link SearchOperation}.  The predicates are combined by the
 * {@link SpecificationBuilder} to form complex queries.
 *
 * @param <T> the entity type
 */
public record GenericSpecification<T>(SearchCriteria criteria) implements Specification<T> {

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        String key = criteria.key();
        Object value = criteria.value();
        SearchOperation op = criteria.operation();

        Path<?> path = root.get(key);

        return switch (op) {
            case GREATER_THAN -> builder.greaterThan(path.as(String.class), value.toString());
            case LESS_THAN -> builder.lessThan(path.as(String.class), value.toString());
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(path.as(String.class), value.toString());
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(path.as(String.class), value.toString());
            case NOT_EQUAL -> builder.notEqual(path, value);
            case EQUAL -> builder.equal(path, value);
            case MATCH ->
                    builder.like(builder.lower(path.as(String.class)), "%" + value.toString().toLowerCase() + "%");
            case MATCH_START ->
                    builder.like(builder.lower(path.as(String.class)), value.toString().toLowerCase() + "%");
            case MATCH_END -> builder.like(builder.lower(path.as(String.class)), "%" + value.toString().toLowerCase());
            case IN -> {
                if (value instanceof Collection<?> collectionValue) {
                    CriteriaBuilder.In<Object> inClause = builder.in(path);
                    for (Object v : collectionValue) {
                        inClause.value(v);
                    }
                    yield inClause;
                }
                // fall through to equality if value is not a collection
                yield builder.equal(path, value);
                // fall through to equality if value is not a collection
            }
            case NOT_IN -> {
                if (value instanceof Collection<?> collectionValue2) {
                    CriteriaBuilder.In<Object> notInClause = builder.in(path);
                    for (Object v : collectionValue2) {
                        notInClause.value(v);
                    }
                    yield builder.not(notInClause);
                }
                yield builder.notEqual(path, value);
            }
        };
    }
}