package com.hnh.enterprise.core.service.impl;

import com.hnh.enterprise.core.constants.Constants;
import com.hnh.enterprise.core.entity.Authority;
import com.hnh.enterprise.core.entity.User;
import com.hnh.enterprise.core.enumeration.AuthProvider;
import com.hnh.enterprise.core.repository.AuthorityRepository;
import com.hnh.enterprise.core.repository.UserRepository;
import com.hnh.enterprise.core.security.AuthoritiesConstants;
import com.hnh.enterprise.core.service.UserService;
import com.hnh.enterprise.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    
    @Override
    public User createOrFindFirebaseUser(String email, String name, @Nullable String avatarUrl) {
        // Try to find an existing user by email first
        return userRepository.findOneWithAuthoritiesByLogin(email).orElseGet(() -> {
            // Create a new user from Firebase info
            log.debug("Creating new user from Firebase token for email: {}", email);
            
            User newUser = new User();
            
            newUser.setLogin(email.toLowerCase());
            newUser.setEmail(email.toLowerCase());
            
            // Parse name if provided
            if (name != null && !name.trim().isEmpty()) {
                String[] nameParts = name.trim().split("\\s+", 2);
                newUser.setFirstName(nameParts[0]);
                if (nameParts.length > 1) {
                    newUser.setLastName(nameParts[1]);
                }
            }
            
            // Set default values
            newUser.setPassword(passwordEncoder.encode(RandomUtil.generatePassword())); // Random password since they login via Firebase
            newUser.setActivated(true); // Firebase users are pre-verified
            newUser.setLangKey(Constants.DEFAULT_LANGUAGE);
            newUser.setAuthProvider(AuthProvider.GOOGLE);
            
            // Set default USER authority
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById(AuthoritiesConstants.USER.getValue()).ifPresent(authorities::add);
            newUser.setAuthorities(authorities);
            
            newUser = userRepository.save(newUser);
            
            log.debug("Created new Firebase user: {}", newUser);
            return newUser;
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }
}
