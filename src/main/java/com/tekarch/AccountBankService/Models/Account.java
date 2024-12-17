package com.tekarch.AccountBankService.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // Foreign key for the User

    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 20)
    private String accountType;

    @Column(name = "balance", precision = 15, scale = 2, columnDefinition = "DECIMAL(15, 2) DEFAULT 0.0")
    private BigDecimal balance = BigDecimal.valueOf(0.0);

    @Column(name = "currency", length = 10, columnDefinition = "VARCHAR(10) DEFAULT 'USD'")
    private String currency = "USD";  // Default currency set to USD

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // If you want to associate Account with User entity (Optional)
   // @ManyToOne
    //@JoinColumn(name = "user_id", referencedColumnName = "userId", insertable = false, updatable = false)
   // private User user;  // Represents the User entity, not just the userId (foreign key)

}
