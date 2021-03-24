package com.app.dao;

import java.util.List;

import com.app.exception.BusinessException;
import com.app.model.BankAccount;
import com.app.model.Transaction;

public interface BankAccountDAO {
	public List<BankAccount> getAccountsByCustomerID(int customerid) throws BusinessException;

	public List<BankAccount> getPendingAccounts() throws BusinessException;

	public List<Transaction> getAccountTransactions() throws BusinessException;

	public BankAccount getAccountById(int accountId) throws BusinessException;

	public boolean isAccountPresent(int accountNumber) throws BusinessException;

	public int depositWithdrawTransaction(int accountId, float accountBalance, float depositAmt,
			String transactionType) throws BusinessException;

	public int transferToTransaction(BankAccount sourceAccount, BankAccount destinationAccount,
			float transferAmt) throws BusinessException;

	public int updateAccountStausbyId(int accountId, String status) throws BusinessException;

	public int createAccountRecord(BankAccount newAccount) throws BusinessException;	
}
