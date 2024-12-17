package com.tekarch.AccountBankService.Repositories;
import com.tekarch.AccountBankService.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AccountRepository extends JpaRepository <Account,Long>{
    // List<Account> findByUserId(Long userId);
    //Optional<Account> findByAccountId(Long accountId);
    // Optional<Account> findByUserIdAndAccountId(Long userId, Long accountId);
    // Find all accounts by userId
    List<Account> findByUserId(Long userId);

    // Find an account by accountId
    Account findByAccountId(Long accountId);

    // Find an account by both userId and accountId
    Account findByUserIdAndAccountId(Long userId, Long accountId);
}

