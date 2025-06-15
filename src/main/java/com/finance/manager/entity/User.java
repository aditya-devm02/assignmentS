// User.java (Entity)
package com.finance.manager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a user in the Personal Finance Manager system.
 * This class stores user information and maintains relationships with other entities
 * such as transactions, categories, and savings goals.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    /**
     * Unique identifier for the user.
     * Automatically generated using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User's email address, which serves as the username.
     * Must be unique across all users.
     */
    @NotBlank
    @Email
    @Column(unique = true)
    private String username;

    /**
     * User's encrypted password.
     * Must not be blank.
     */
    @NotBlank
    private String password;

    /**
     * User's full name.
     * Must not be blank.
     */
    @NotBlank
    private String fullName;

    /**
     * User's phone number.
     * Must start with '+' and contain 10 to 15 digits.
     */
    @NotBlank
    @Pattern(regexp = "^\\+[0-9]{10,15}$", message = "Phone number must start with '+' and contain 10 to 15 digits")
    private String phoneNumber;
    

    /**
     * Collection of transactions associated with this user.
     * Uses one-to-many relationship with cascade operations.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new HashSet<>();

    /**
     * Collection of categories associated with this user.
     * Uses one-to-many relationship with cascade operations.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    /**
     * Collection of savings goals associated with this user.
     * Uses one-to-many relationship with cascade operations.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<SavingsGoal> savingsGoals = new HashSet<>();
}
