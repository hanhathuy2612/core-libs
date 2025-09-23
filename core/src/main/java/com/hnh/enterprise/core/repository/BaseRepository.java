package com.hnh.enterprise.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * Generic base repository that combines the standard JPA repository with
 * Spring Data's specification executor. All repositories should extend
 * this interface to gain access to basic CRUD operations and dynamic
 * query specification support.
 *
 * @param <T>  the domain type the repository manages
 * @param <ID> the type of the id of the entity the repository manages
 */
public interface BaseRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    // Place for common repository functionality, for example bulk operations.

}