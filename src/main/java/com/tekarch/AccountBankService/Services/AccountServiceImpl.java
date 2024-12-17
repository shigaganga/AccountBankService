package com.tekarch.AccountBankService.Services;

import com.tekarch.AccountBankService.Models.Account;
import com.tekarch.AccountBankService.Repositories.AccountRepository;
import com.tekarch.AccountBankService.Services.Interface.AccountService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Get all accounts
    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Get accounts by User ID
    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    // Get a single account by Account ID
    @Override
    public Account getAccountByAccountId(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        return (account != null) ? account : null; // Return null if not found
    }

    // Update an account by Account ID
    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        Account existingAccount = accountRepository.findByAccountId(accountId);
        if (existingAccount != null) {
            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());  // Update account number
            existingAccount.setAccountType(updatedAccount.getAccountType());      // Update account type
            existingAccount.setBalance(updatedAccount.getBalance());              // Update balance
            existingAccount.setCurrency(updatedAccount.getCurrency());            // Update currency
            return accountRepository.save(existingAccount);
        }
        return null;  // If account is not found
    }

    // Update account using User ID and Account ID
    @Override
    public Account updateAccountByUserIdAndAccountId(Long userId, Long accountId, Account updatedAccount) {
        Account existingAccount = accountRepository.findByUserIdAndAccountId(userId, accountId);
        if (existingAccount != null) {
            existingAccount.setAccountNumber(updatedAccount.getAccountNumber());  // Update account number
            existingAccount.setAccountType(updatedAccount.getAccountType());      // Update account type
            existingAccount.setBalance(updatedAccount.getBalance());              // Update balance
            existingAccount.setCurrency(updatedAccount.getCurrency());            // Update currency
            return accountRepository.save(existingAccount);
        }
        return null;  // If account is not found
    }

    // Delete an account by Account ID
    @Override
    public boolean deleteAccount(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        if (account != null) {
            accountRepository.delete(account);
            return true;
        }
        return false;  // If account not found
    }

    // Fetch all accounts' balances by User ID
    @Override
    public List<Account> getBalancesByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    // Get the balance of a specific account by Account ID
    @Override
    public BigDecimal getBalanceByAccountId(Long accountId) {
        Account account = accountRepository.findByAccountId(accountId);
        return (account != null) ? account.getBalance() : null; // Return null if not found

    }

}