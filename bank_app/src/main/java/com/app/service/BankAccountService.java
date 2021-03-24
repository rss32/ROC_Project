package com.app.service;

import java.util.List;

import com.app.exception.BusinessException;
import com.app.model.BankAccount;
import com.app.model.Transaction;

public interface BankAccountService {

	public List<BankAccount> getBankAccounts(int custAcctNo) throws BusinessException;

	public List<BankAccount> filterActiveAccounts(List<BankAccount> bankAccount);

	public List<BankAccount> filterPendingAccounts(List<BankAccount> bankAccount);

	public List<BankAccount> filterRejectedAccounts(List<BankAccount> bankAccount);

	public List<BankAccount> getPendingBankAccounts() throws BusinessException;
	
	public List<Transaction> getAllTransactions() throws BusinessException;
	
	public BankAccount depositTransaction(BankAccount account, float depositAmt)
			throws BusinessException;

	public BankAccount withdrawalTransaction(BankAccount account, float withdrawAmt)
			throws BusinessException;

	public BankAccount transferToTransaction(BankAccount sourceAccount, int transferAcctNum,
			float transferAmt) throws BusinessException;

	public boolean validateNewBankAccountInfo(BankAccount ba) throws BusinessException;

	public int updateBankAccountStatus(BankAccount ba) throws BusinessException;

	public int createBankAccount(BankAccount ba) throws BusinessException;

}
