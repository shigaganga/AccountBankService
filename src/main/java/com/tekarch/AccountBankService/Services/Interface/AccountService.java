package com.tekarch.AccountBankService.Services.Interface;

import com.tekarch.AccountBankService.Models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    List<Account> getAllAccounts();
    List<Account> getAccountsByUserId(Long userId);
    Account getAccountByAccountId(Long accountId);
    Account updateAccount(Long accountId, Account updatedAccount);
    Account updateAccountByUserIdAndAccountId(Long userId, Long accountId, Account updatedAccount);
    boolean deleteAccount(Long accountId);
    List<Account> getBalancesByUserId(Long userId);
    BigDecimal getBalanceByAccountId(Long accountId);
     BigDecimal getTransactionLimit(Long accountId);
}
