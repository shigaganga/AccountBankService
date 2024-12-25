package com.tekarch.AccountBankService.Services;

import com.tekarch.AccountBankService.DTO.UserDTO;
import com.tekarch.AccountBankService.Exceptions.UserNotFoundException;
import com.tekarch.AccountBankService.Models.Account;
import com.tekarch.AccountBankService.Repositories.AccountRepository;
import com.tekarch.AccountBankService.Services.Interface.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor // Lombok annotation to generate a constructor for final fields
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    // Dynamic User Microservice URL from application properties
    @Value("${user.ms.url:http://localhost:8080/users}")
    private String userMsUrl;

    private final RestTemplate restTemplate;
    private final AccountRepository accountRepository;

    /**
     * Validates if the user exists by calling the User Microservice.
     *
     * @param userId The ID of the user to validate.
     * @return True if the user exists, false otherwise.
     */
    private boolean isUserExists(Long userId) {
        String url = userMsUrl + "/" + userId;
        try {
            logger.info("Validating user existence for User ID: {}", userId);
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    UserDTO.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            logger.error("Error validating user existence for User ID {}: {}", userId, e.getMessage());
            return false;
        }
    }

    @Override
    public Account createAccount(Account account) {
        if (isUserExists(account.getUserId())) {
            logger.info("Creating account for User ID: {}", account.getUserId());
            return accountRepository.save(account);
        } else {
            logger.error("User with ID {} not found. Account creation failed.", account.getUserId());
            throw new UserNotFoundException("User with ID " + account.getUserId() + " not found. Account Creation Failed");
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        logger.info("Fetching all accounts.");
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        if (isUserExists(userId)) {
            logger.info("Fetching accounts for User ID: {}", userId);
            return accountRepository.findByUserId(userId);
        } else {
            throw new UserNotFoundException("User ID " + userId + " not found.");
        }
    }

    @Override
    public Account getAccountByAccountId(Long accountId) {
        logger.info("Fetching account with ID: {}", accountId);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("Account with ID " + accountId + " not found."));
    }

    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        logger.info("Updating account with ID: {}", accountId);
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("Account with ID " + accountId + " not found."));

        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setAccountType(updatedAccount.getAccountType());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setCurrency(updatedAccount.getCurrency());

        return accountRepository.save(existingAccount);
    }

    @Override
    public Account updateAccountByUserIdAndAccountId(Long userId, Long accountId, Account updatedAccount) {
        if (isUserExists(userId)) {
            logger.info("Updating account for User ID {} and Account ID {}", userId, accountId);
            Account existingAccount = accountRepository.findByUserIdAndAccountId(userId, accountId);
            if (existingAccount != null) {
                existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
                existingAccount.setAccountType(updatedAccount.getAccountType());
                existingAccount.setBalance(updatedAccount.getBalance());
                existingAccount.setCurrency(updatedAccount.getCurrency());
                return accountRepository.save(existingAccount);
            } else {
                logger.error("Account with User ID {} and Account ID {} not found.", userId, accountId);
                throw new UserNotFoundException("Account not found for User ID " + userId + " and Account ID " + accountId);
            }
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
    }

    @Override
    public boolean deleteAccount(Long accountId) {
        logger.info("Deleting account with ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("Account with ID " + accountId + " not found."));
        accountRepository.delete(account);
        return true;
    }

    @Override
    public List<Account> getBalancesByUserId(Long userId) {
        if (isUserExists(userId)) {
            logger.info("Fetching balances for User ID: {}", userId);
            return accountRepository.findByUserId(userId);
        } else {
            throw new UserNotFoundException("User ID " + userId + " not found.");
        }
    }

    @Override
    public BigDecimal getBalanceByAccountId(Long accountId) {
        logger.info("Fetching balance for Account ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("Account with ID " + accountId + " not found."));
        return account.getBalance();
    }
}
