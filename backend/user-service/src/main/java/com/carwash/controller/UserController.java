package com.carwash.controller;

import com.carwash.entity.User;
import com.carwash.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.lang.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for User management operations
 * Provides HTTP endpoints for user CRUD operations
 */
@RestController
@RequestMapping("/api/users")
// CORS handled globally via CorsConfig
public class UserController {
    
    private final UserService userService;
    
    public UserController(@Nullable UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Create a new user
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Return bad request if user already exists or validation fails
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all users
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get user by ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get user by email
     * GET /api/users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get users by role
     * GET /api/users/role/{role}
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable User.Role role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Update user
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            // Return not found if user doesn't exist or bad request for validation errors
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Delete user
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Return not found if user doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Search users by name
     * GET /api/users/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Get user statistics by role
     * GET /api/users/stats/{role}
     */
    @GetMapping("/stats/{role}")
    public ResponseEntity<Long> getUserCountByRole(@PathVariable User.Role role) {
        long count = userService.getUserCountByRole(role);
        return ResponseEntity.ok(count);
    }
}