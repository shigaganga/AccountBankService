package com.tekarch.AccountBankService.Services;

import com.tekarch.AccountBankService.DTO.UserDTO;
import com.tekarch.AccountBankService.Models.Account;
import com.tekarch.AccountBankService.Repositories.AccountRepository;
import com.tekarch.AccountBankService.Services.Interface.AccountService;
import com.tekarch.AccountBankService.Exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;

    private final RestTemplate restTemplate;

    private final String USER_MS_URL = "http://localhost:8090/users"; // Hardcoded User Microservice Base URL

    // Helper method to check if User exists in User Microservice using RestTemplate.exchange
    private boolean isUserExists(Long userId) {
        String url = USER_MS_URL + "/" + userId; // e.g., http://localhost:8090/users/1
        HttpEntity<Void> requestEntity = new HttpEntity<>(null); // Empty request entity

        try {
            logger.info("Validating user existence for User ID: {}", userId);

            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    UserDTO.class
            );

            return response.getStatusCode() == HttpStatus.OK; // Return true if status is 200 OK
        } catch (Exception e) {
            logger.error("User validation failed for User ID {}: {}", userId, e.getMessage());
            return false; // Return false if user does not exist or an error occurs
        }
    }

    // Create a new account after validating User ID
    @Override
    public Account createAccount(Account account) {
        if (isUserExists(account.getUserId())) {
            logger.info("User with ID {} exists. Proceeding with account creation.", account.getUserId());
            return accountRepository.save(account);
        } else {
            logger.error("User with ID {} not found. Cannot create account.", account.getUserId());
            throw new UserNotFoundException("User with ID " + account.getUserId() + " not found.");
        }
    }

    // Fetch all accounts
    @Override
    public List<Account> getAllAccounts() {
        logger.info("Fetching all accounts.");
        return accountRepository.findAll();
    }

    // Get accounts by User ID
    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        if (isUserExists(userId)) {
            logger.info("Fetching accounts for User ID: {}", userId);
            return accountRepository.findByUserId(userId);
        } else {
            throw new UserNotFoundException("User ID " + userId + " not found.");
        }
    }

    // Get a single account by Account ID
    @Override
    public Account getAccountByAccountId(Long accountId) {
        logger.info("Fetching account with ID: {}", accountId);
        return accountRepository.findByAccountId(accountId);
    }

    // Update an account by Account ID
    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        logger.info("Updating account with ID: {}", accountId);
        Account existingAccount = accountRepository.findByAccountId(accountId);
        if (existingAccount != null) {
            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
            existingAccount.setAccountType(updatedAccount.getAccountType());
            existingAccount.setBalance(updatedAccount.getBalance());
            existingAccount.setCurrency(updatedAccount.getCurrency());
            return accountRepository.save(existingAccount);
        }
        logger.error("Account with ID {} not found.", accountId);
        return null;
    }

    // Update account using User ID and Account ID
    @Override
    public Account updateAccountByUserIdAndAccountId(Long userId, Long accountId, Account updatedAccount) {
        if (isUserExists(userId)) {
            logger.info("Updating account with User ID {} and Account ID {}", userId, accountId);
            Account existingAccount = accountRepository.findByUserIdAndAccountId(userId, accountId);
            if (existingAccount != null) {
                existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
                existingAccount.setAccountType(updatedAccount.getAccountType());
                existingAccount.setBalance(updatedAccount.getBalance());
                existingAccount.setCurrency(updatedAccount.getCurrency());
                return accountRepository.save(existingAccount);
            }
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        return null;
    }

    // Delete an account by Account ID
    @Override
    public boolean deleteAccount(Long accountId) {
        logger.info("Deleting account with ID: {}", accountId);
        Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            accountRepository.delete(account);
            return true;
        }
        logger.error("Account with ID {} not found.", accountId);
        return false;
    }

    // Fetch all accounts' balances by User ID
    @Override
    public List<Account> getBalancesByUserId(Long userId) {
        if (isUserExists(userId)) {
            logger.info("Fetching balances for User ID: {}", userId);
            return accountRepository.findByUserId(userId);
        } else {
            throw new UserNotFoundException("User ID " + userId + " not found.");
        }
    }

    // Get the balance of a specific account by Account ID
    @Override
    public BigDecimal getBalanceByAccountId(Long accountId) {
        logger.info("Fetching balance for Account ID: {}", accountId);
        Account account = accountRepository.findByAccountId(accountId);
        return (account != null) ? account.getBalance() : null;
    }
}
