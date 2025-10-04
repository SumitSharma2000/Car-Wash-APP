package com.carwash.repository;

import com.carwash.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations
 * Provides database access methods for user management
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address
     * Used for authentication and user lookup
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find users by role (CUSTOMER or SERVICE_PROVIDER)
     * Useful for role-based queries and filtering
     */
    List<User> findByRole(User.Role role);
    
    /**
     * Check if user exists by email
     * Used for validation during user registration
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by name containing search term (case-insensitive)
     * Enables user search functionality
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Count users by role
     * Provides statistics for dashboard and reporting
     */
    long countByRole(User.Role role);
}