package com.tekarch.AccountBankService.Controller;

import com.tekarch.AccountBankService.Models.Account;
import com.tekarch.AccountBankService.Services.AccountServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;
    private static final Logger logger = LogManager.getLogger(AccountController.class);

    // Create a new account
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        logger.info("Creating new account for User ID: {}", account.getUserId());
        Account createdAccount = accountServiceImpl.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        logger.info("Fetching all accounts");
        logger.info("Fetching all account");
        List<Account> accountList = accountServiceImpl.getAllAccounts();
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    // Get accounts by User ID
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        logger.info("Fetching accounts for User ID: {}", userId);
        List<Account> accounts = accountServiceImpl.getAccountsByUserId(userId);
        return accounts.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(accounts, HttpStatus.OK);
    }
    //Get accounts by AccountId
    @GetMapping("/{accountId}")
    public ResponseEntity<Account>getAccount(@PathVariable Long accountId){
        logger.info("getting an account with accountId:{}",accountId);
        try {
// Fetch the account
            Account getAccount = accountServiceImpl.getAccountByAccountId(accountId);
// Return the account with HTTP status 200 OK
            return ResponseEntity.ok(getAccount);
        } catch (Exception e) {
// Log the error and return 404 Not Found if the account doesn't exist
            logger.error("Error fetching account with accountId {}: {}", accountId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Update an account by Account ID
    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId, @RequestBody Account account) {
        logger.info("Updating account with ID: {}", accountId);
        Account updatedAccount = accountServiceImpl.updateAccount(accountId, account);
        return updatedAccount != null ? new ResponseEntity<>(updatedAccount, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update account using User ID and Account ID
    @PutMapping("/update")
    public ResponseEntity<Account> updateAccountByUserIdAndAccountId(
            @RequestParam Long userId, @RequestParam Long accountId, @RequestBody Account account) {
        logger.info("Updating account for User ID: {} and Account ID: {}", userId, accountId);
        Account updatedAccount = accountServiceImpl.updateAccountByUserIdAndAccountId(userId, accountId, account);
        return updatedAccount != null ? new ResponseEntity<>(updatedAccount, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get account balance by Account ID using query parameter
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalanceByQueryParam(@RequestParam Long accountId) {
        logger.info("Fetching balance for Account ID: {}", accountId);
        BigDecimal balance = accountServiceImpl.getBalanceByAccountId(accountId);
        return balance != null ? new ResponseEntity<>(balance, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get account balance by Account ID using path parameter
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalanceByPathParam(@PathVariable Long accountId) {
        logger.info("Fetching balance for Account ID: {}", accountId);
        BigDecimal balance = accountServiceImpl.getBalanceByAccountId(accountId);
        return balance != null ? new ResponseEntity<>(balance, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Delete an account by Account ID
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        logger.info("Deleting account with ID: {}", accountId);
        boolean isDeleted = accountServiceImpl.deleteAccount(accountId);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/{accountId}/transactionlimit")
    public ResponseEntity<BigDecimal> getTransactionLimit(@PathVariable Long accountId) {
        BigDecimal transactionLimit = accountServiceImpl.getTransactionLimit(accountId);
        return transactionLimit != null
                ? new ResponseEntity<>(transactionLimit, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Exception Handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception e) {
        logger.error("Exception occurred: {}", e.getMessage());
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}