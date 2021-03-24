package com.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.app.dao.BankAccountDAO;
import com.app.dao.impl.BankAccountDAOImpl;
import com.app.exception.BusinessException;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import com.app.service.BankAccountService;

public class BankAccountServiceImpl implements BankAccountService {

	private BankAccountDAO baDAO = new BankAccountDAOImpl();

	@Override
	public List<BankAccount> getBankAccounts(int custAcctNo) throws BusinessException {
		return baDAO.getAccountsByCustomerID(custAcctNo);
	}

	/*
	 * getActiveAccounts(List<BankAccount>) returns the account with the status "active" using the
	 * lambda expression;
	 */
	@Override
	public List<BankAccount> filterActiveAccounts(List<BankAccount> bankAccount) {

		return bankAccount.stream().filter(baInstance -> baInstance.getStatus().equals("active"))
				.collect(Collectors.toList());

	}

	/*
	 * getPendingAccounts(List<BankAccount>) returns the account with the status "pending" using the
	 * lambda expression;
	 */
	@Override
	public List<BankAccount> filterPendingAccounts(List<BankAccount> bankAccount) {
		return bankAccount.stream().filter(baInstance -> baInstance.getStatus().equals("pending"))
				.collect(Collectors.toList());
	}

	/*
	 * getRejectedAccounts(List<BankAccount>) returns the account with the status "rejected" using
	 * the lambda expression;
	 */
	@Override
	public List<BankAccount> filterRejectedAccounts(List<BankAccount> bankAccount) {

		return bankAccount.stream().filter(baInstance -> baInstance.getStatus().equals("rejected"))
				.collect(Collectors.toList());
	}

	@Override
	public List<BankAccount> getPendingBankAccounts() throws BusinessException {
		return baDAO.getPendingAccounts();
	}

	@Override
	public List<Transaction> getAllTransactions() throws BusinessException {
		return baDAO.getAccountTransactions();
	}
	
	@Override
	public BankAccount depositTransaction(BankAccount account, float depositAmt)
			throws BusinessException {
		if (!BankAccountValidation.isNonNegativeNumber(depositAmt)) {
			throw new BusinessException("Amount cannot be negative. Please try again.");
		}
		float currentBalance = account.getBalance();

		account.setBalance(currentBalance + depositAmt);

		int update =baDAO.depositWithdrawTransaction(account.getAccountId(), account.getBalance(), depositAmt,
				"deposit");
		
		if (update < 2){		
			throw new BusinessException(
					String.format("Could not deposit amt $ %.2f", depositAmt));
		}		

		return account;
	}

	@Override
	public BankAccount withdrawalTransaction(BankAccount account, float withdrawAmt)
			throws BusinessException {
		if (!BankAccountValidation.isNonNegativeNumber(withdrawAmt)) {
			throw new BusinessException("Amount cannot be negative. Please try again.");
		}
		if (!BankAccountValidation.isBalanceGreater(account.getBalance(), withdrawAmt)) {
			throw new BusinessException(
					"Not enough money in account to perform trasnaction. Please try again.");
		}
		float currentBalance = account.getBalance();

		account.setBalance(currentBalance - withdrawAmt);
		
		int update = baDAO.depositWithdrawTransaction(account.getAccountId(), account.getBalance(), withdrawAmt,
				"withdraw");

		if (update < 2){		
			throw new BusinessException(
					String.format("Could not withdraw amt $ %.2f", withdrawAmt));
		}
		
		return account;
	}

	@Override
	public BankAccount transferToTransaction(BankAccount sourceAccount, int transferAcctNum,
			float transferAmt) throws BusinessException {

		if (!BankAccountValidation.isAccountCorrectFormat(transferAcctNum)) {
			throw new BusinessException("Account is not in correct format. Please try again");
		}

		// Step 1- get BankAccount of the recipient if there is one.
		BankAccount destinationAccount = baDAO.getAccountById(transferAcctNum);

		/*
		 * Step 2 - check if recipient account can recieve transactions. rejected accounts won't be
		 * able to accept transactions also if the transfer is to the same account do not continue
		 * with transaction.
		 */
		if (destinationAccount.getStatus().equals("rejected")
				|| (destinationAccount.getAccountId() == sourceAccount.getAccountId())) {
			throw new BusinessException("You cannot transfer money to this account.");
		}

		// Step 3- Check if the transfer amount is valid
		if (!BankAccountValidation.isNonNegativeNumber(transferAmt)) {
			throw new BusinessException("Amount cannot be negative. Please try again.");
		}
		if (!BankAccountValidation.isBalanceGreater(sourceAccount.getBalance(), transferAmt)) {
			throw new BusinessException(
					"Not enough money in account to perform trasnaction. Please try again.");
		}

		// Step 4- if everything checks out complete the transfer
		float currentBalance = sourceAccount.getBalance();
		sourceAccount.setBalance(currentBalance - transferAmt);

		float destinationBalance = destinationAccount.getBalance();
		destinationAccount.setBalance(destinationBalance + transferAmt);

		int numUpdates = baDAO.transferToTransaction(sourceAccount, destinationAccount,
				transferAmt);
		if (numUpdates < 4) {
			throw new BusinessException("Error in inserting new customer.");
		}

		return sourceAccount;
	}

	@Override
	public boolean validateNewBankAccountInfo(BankAccount ba) throws BusinessException {

		if (!BankAccountValidation.isCorrectAccountType(ba.getAcctType())) {
			throw new BusinessException(
					"Bank account type must be either 'checking' or 'savings'.");
		}

		if (!BankAccountValidation.isNonNegativeNumber(ba.getBalance())) {
			throw new BusinessException("You cannot have a negative balance.");
		}

		/*
		 * return true if everything checks out. The method will fail if one of the details is
		 * invalid
		 */
		return true;
	}
	
	@Override
	public int updateBankAccountStatus(BankAccount ba) throws BusinessException {
		
		int update = baDAO.updateAccountStausbyId(ba.getAccountId(), ba.getStatus());
				
				if (update == 0) {

					throw new BusinessException("Account status could not be updated.");
				}
		return  update;
	}	

	@Override
	public int createBankAccount(BankAccount ba) throws BusinessException {

		int update = baDAO.createAccountRecord(ba);
		if (update == 0) {
			throw new BusinessException("Account status could not be updated.");
		}
		return update;		
	}

}
