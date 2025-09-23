package com.hnh.enterprise.core.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for composing multiple {@link SearchCriteria} into a single
 * {@link Specification}.  Accumulates criteria and produces a
 * conjunction of predicates when built.  If no criteria are supplied the
 * builder returns {@code null}, allowing the caller to decide whether
 * to apply a specification or not.
 *
 * @param <T> the entity type being queried
 */
public class SpecificationBuilder<T> {

    private final List<SearchCriteria> params = new ArrayList<>();

    /**
     * Adds a new search criteria to the builder.  Chaining multiple calls
     * allows building composite queries.
     *
     * @param key       the field name in the entity
     * @param operation the comparison operation
     * @param value     the value to compare against
     * @return the builder for fluent chaining
     */
    public SpecificationBuilder<T> with(String key, SearchOperation operation, Object value) {
        if (key != null && operation != null && value != null) {
            params.add(new SearchCriteria(key, operation, value));
        }
        return this;
    }

    /**
     * Builds a {@link Specification} representing the conjunction of all
     * criteria supplied to the builder.  Returns {@code null} if no
     * criteria were provided.
     *
     * @return a composed Specification or null
     */
    public Specification<T> build() {
        if (params.isEmpty()) {
            return null;
        }
        Specification<T> result = new GenericSpecification<>(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new GenericSpecification<>(params.get(i)));
        }
        return result;
    }
}