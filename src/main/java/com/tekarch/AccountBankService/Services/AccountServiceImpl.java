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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Value("${user.ms.url}")
    private String userMsUrl;

    private final RestTemplate restTemplate;
    private final AccountRepository accountRepository;

    /**
     * Validates if a user exists by calling the User Microservice.
     *
     * @param userId User ID to validate.
     * @return true if the user exists, false otherwise.
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
        } catch (RestClientException e) {
            logger.error("I/O error occurred while validating user existence for User ID {}. URL: {}. Error: {}", userId, url, e.getMessage());
            return false;
        }
    }


    @Override
    public Account createAccount(Account account) {
        if (!isUserExists(account.getUserId())) {
            logger.error("User with ID {} not found. Account creation failed.", account.getUserId());
            throw new UserNotFoundException("User with ID " + account.getUserId() + " not found.");
        }
        logger.info("Creating new account for User ID: {}", account.getUserId());
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        logger.info("Retrieving all accounts.");
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        if (!isUserExists(userId)) {
            logger.error("User with ID {} not found. Unable to retrieve accounts.", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        logger.info("Fetching accounts for User ID: {}", userId);
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account getAccountByAccountId(Long accountId) {
        logger.info("Fetching account with Account ID: {}", accountId);
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
        if (!isUserExists(userId)) {
            logger.error("User with ID {} not found. Update operation failed.", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        logger.info("Updating account with ID {} for User ID {}", accountId, userId);
        Account existingAccount = Optional.ofNullable(accountRepository.findByUserIdAndAccountId(userId, accountId))
                .orElseThrow(() -> new UserNotFoundException("Account not found for User ID " + userId + " and Account ID " + accountId));

        existingAccount.setAccountNumber(updatedAccount.getAccountNumber());
        existingAccount.setAccountType(updatedAccount.getAccountType());
        existingAccount.setBalance(updatedAccount.getBalance());
        existingAccount.setCurrency(updatedAccount.getCurrency());

        return accountRepository.save(existingAccount);
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
        if (!isUserExists(userId)) {
            logger.error("User ID {} not found. Unable to retrieve balances.", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
        logger.info("Fetching balances for User ID: {}", userId);
        return accountRepository.findByUserId(userId);
    }

    @Override
    public BigDecimal getBalanceByAccountId(Long accountId) {
        logger.info("Fetching balance for Account ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new UserNotFoundException("Account with ID " + accountId + " not found."));
        return account.getBalance();
    }
    public BigDecimal getTransactionLimit(Long accountId) {
        // Assuming static limit of 10,000 for all accounts
        return new BigDecimal("10000.00");
    }

}
