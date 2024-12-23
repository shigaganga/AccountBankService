
package com.tekarch.AccountBankService.DTO;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
@Getter
@Setter
public class UserDTO {
    private Long userId;               // user_id (Primary Key)
    private String username;           // UNIQUE, NOT NULL
    private String email;              // UNIQUE, NOT NULL
    private String phoneNumber;        // UNIQUE, NULLABLE
    private String passwordHash;
    private Boolean twoFactorEnabled=false;  // Default: FALSE
    private String kycStatus="pending";          // Default: 'pending'
    private Timestamp createdAt;       // Default: CURRENT_TIMESTAMP
    private Timestamp updatedAt;       // Default: CURRENT_TIMESTAMP
}




