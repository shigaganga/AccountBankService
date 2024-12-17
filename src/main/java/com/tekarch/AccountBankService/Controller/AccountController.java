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
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;
    private static final Logger logger = LogManager.getLogger(AccountController.class);

    // Create a new account
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        logger.info("Creating new account");
        Account createdAccount = accountServiceImpl.createAccount(account);
        logger.info("New account created with ID: {}", createdAccount.getAccountId());
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Get all accounts
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accountList = accountServiceImpl.getAllAccounts();
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    // Get a list of accounts by User ID
    @GetMapping("users/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        List<Account> accounts = accountServiceImpl.getAccountsByUserId(userId);
        return accounts.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Get a single account by Account ID
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountServiceImpl.getAccountByAccountId(accountId);
        return account != null ? new ResponseEntity<>(account, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update an account by Account ID
    @PutMapping("/account/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long accountId, @RequestBody Account account) {
        logger.info("Updating account with ID: {}", accountId);
        Account updatedAccount = accountServiceImpl.updateAccount(accountId, account);
        return updatedAccount != null ? new ResponseEntity<>(updatedAccount, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update account details using User ID and Account ID as Query Parameters
    @PutMapping("/account")
    public ResponseEntity<Account> updateAccountByUserIdAndAccountId(@RequestParam Long userId, @RequestParam Long accountId, @RequestBody Account account) {
        logger.info("Updating account with User ID: {} and Account ID: {}", userId, accountId);
        Account updatedAccount = accountServiceImpl.updateAccountByUserIdAndAccountId(userId, accountId, account);
        return updatedAccount != null ? new ResponseEntity<>(updatedAccount, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete an account by Account ID
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        boolean isDeleted = accountServiceImpl.deleteAccount(accountId);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Fetch balances for all accounts of a user by using Path Parameter
    @GetMapping("/user/{userId}/accountss/balance")
    public ResponseEntity<List<Account>> getBalancesByUserId(@PathVariable Long userId) {
        List<Account> accounts = accountServiceImpl.getBalancesByUserId(userId);
        return accounts.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Fetch balances for all accounts of a user using Query Parameter
    @GetMapping("users/{userId}/account/balance?userId=value")
    public ResponseEntity<List<Account>> getBalancesByUserIdQueryParam(@RequestParam Long userId) {
        List<Account> accounts = accountServiceImpl.getBalancesByUserId(userId);
        return accounts.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Get account balance by Account ID using Path Parameter
    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalanceByAccountId(@PathVariable Long accountId) {
        BigDecimal balance = accountServiceImpl.getBalanceByAccountId(accountId);
        return balance != null ? new ResponseEntity<>(balance, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Get account balance by Account ID using Query Parameter
    @GetMapping("/account/balance?accountId=value")
    public ResponseEntity<BigDecimal> getBalanceByAccountIdQueryParam(@RequestParam Long accountId) {
        BigDecimal balance = accountServiceImpl.getBalanceByAccountId(accountId);
        return balance != null ? new ResponseEntity<>(balance, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception e) {
        logger.error("Exception occurred: {}", e.getMessage());
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.BAD_REQUEST);

    }
}