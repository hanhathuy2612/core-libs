package com.hnh.enterprise.core.service;


import com.hnh.enterprise.core.entity.User;

import java.util.Optional;

/**
 * Service class for managing users.
 */

public interface UserService {
    User createOrFindFirebaseUser(String email, String name, String picture);
    
    Optional<User> getUserWithAuthoritiesByLogin(String email);
}
