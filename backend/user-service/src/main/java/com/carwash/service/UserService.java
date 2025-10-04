package com.carwash.service;

import com.carwash.entity.User;
import com.carwash.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for User entity operations
 * Contains business logic for user management
 */
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Create a new user
     * Validates email uniqueness before creation
     */
    public User createUser(User user) {
        // Check if user with email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }
    
    /**
     * Get user by ID
     * Returns user if found, empty optional otherwise
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get user by email
     * Used for authentication and user lookup
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Get all users
     * Returns complete list of users in the system
     */
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Get users by role
     * Filters users based on their role (CUSTOMER or SERVICE_PROVIDER)
     */
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Update user information
     * Updates existing user data
     */
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
            .map(user -> {
                // Update user fields while preserving ID and timestamps
                user.setName(updatedUser.getName());
                user.setPhone(updatedUser.getPhone());
                user.setRole(updatedUser.getRole());
                // Email updates require additional validation
                if (!user.getEmail().equals(updatedUser.getEmail())) {
                    if (userRepository.existsByEmail(updatedUser.getEmail())) {
                        throw new IllegalArgumentException("Email " + updatedUser.getEmail() + " is already in use");
                    }
                    user.setEmail(updatedUser.getEmail());
                }
                return userRepository.save(user);
            })
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
    
    /**
     * Delete user by ID
     * Removes user from the system
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    /**
     * Search users by name
     * Performs case-insensitive search on user names
     */
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
    
    /**
     * Get user count by role
     * Provides statistics for dashboard and reporting
     */
    @Transactional(readOnly = true)
    public long getUserCountByRole(User.Role role) {
        return userRepository.countByRole(role);
    }
}