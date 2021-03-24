
package com.app.dao.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.app.dao.BankAccountDAO;
import com.app.exception.BusinessException;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import com.app.util.dbutil.PostgresConnection;

public class BankAccountDAOImpl implements BankAccountDAO {

	@Override
	public List<BankAccount> getAccountsByCustomerID(int customerid) throws BusinessException {

		List<BankAccount> accountList = new ArrayList<>();
		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "select a.accountid, a.customerid, act.type, acs.status, a.balance,a.opendate "
					+ "from bank_schema.customer c join bank_schema.account a on c.customerid = a.customerid "
					+ "join bank_schema.accounttype act on a.accounttypeid = act.accounttypeid "
					+ "join bank_schema.accountstatus acs on  a.statusid = acs.statusid "
					+ "where c.customerid = ? order by a.accountid asc";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			preparedStatement.setInt(1, customerid);
			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				BankAccount acct = new BankAccount();
				acct.setAccountId(results.getInt("accountid"));
				acct.setCustomerId(results.getInt("customerid"));
				acct.setAcctType(results.getString("type"));
				acct.setStatus(results.getString("status"));
				acct.setBalance(results.getFloat("balance"));
				acct.setOpeningDate(results.getDate("opendate"));
				accountList.add(acct);
			}
			if (accountList.size() == 0) {
				throw new BusinessException("This customer has no bank accounts");
			}
		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException(
					"E-A01 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return accountList;
	}

	@Override
	public List<BankAccount> getPendingAccounts() throws BusinessException {

		List<BankAccount> pendingBankAccountList = new ArrayList<>();

		try (Connection conn = PostgresConnection.getConnection()) {

			// status id is 2 for pending
			String sql = "select a.accountid, a.customerid,act.type,stat.status, "
					+ "a.balance, a.opendate from bank_schema.account a "
					+ "join bank_schema.accounttype act "
					+ "on a.accounttypeid = act.accounttypeid "
					+ "join bank_schema.accountstatus stat on a.statusid = stat.statusid "
					+ "where a.statusid = 2";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				BankAccount ba = new BankAccount();
				ba.setAccountId(results.getInt("accountid"));
				ba.setCustomerId(results.getInt("customerid"));
				ba.setAcctType(results.getString("type"));
				ba.setStatus(results.getString("status"));
				ba.setBalance(results.getFloat("balance"));
				ba.setOpeningDate(results.getDate("opendate"));
				pendingBankAccountList.add(ba);
			}
			if (pendingBankAccountList.size() == 0) {
				throw new BusinessException("No accounts need to be approved.");
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException(
					"E-A02 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}

		return pendingBankAccountList;
	}

	@Override
	public List<Transaction> getAccountTransactions() throws BusinessException {
		List<Transaction> transactionsList = new ArrayList<>();

		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "select tr.transactionid, tr.accountid, tt.type, "
					+ "tr.destinationaccount, tr.transactiondate, tr.amount "
					+ "from bank_schema.transactions tr join bank_schema.transactiontype tt "
					+ "on tr.transactiontypeid = tt.transactiontypeid "
					+ "order by tr.transactionid asc";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			ResultSet results = preparedStatement.executeQuery();
			while (results.next()) {
				Transaction tran = new Transaction();
				tran.setTransactionId(results.getInt("transactionid"));
				tran.setOriginAccount(results.getInt("accountid"));
				tran.setTransactionType(results.getString("type"));
				tran.setDestinationAccount(results.getInt("destinationaccount"));
				tran.setTransactionDate(results.getTimestamp("transactiondate"));
				tran.setTransactionAmt(results.getFloat("amount"));
				transactionsList.add(tran);
			}
			if (transactionsList.size() == 0) {
				throw new BusinessException("No transactions found.");
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException(
					"E-A03 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return transactionsList;
	}

	@Override
	public BankAccount getAccountById(int accountId) throws BusinessException {

		/*
		 * account- returns the account if the account is present or null.
		 */
		BankAccount account = null;

		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "select a.accountid, a.customerid,act.type,stat.status,  a.balance, a.opendate "
					+ "from bank_schema.account a join bank_schema.accounttype act on a.accounttypeid = act.accounttypeid "
					+ "join bank_schema.accountstatus stat on a.statusid = stat.statusid "
					+ "where a.accountid = ? ";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			// Account table
			preparedStatement.setInt(1, accountId);
			ResultSet results = preparedStatement.executeQuery();

			while (results.next()) {
				account = new BankAccount();
				account.setAccountId(results.getInt("accountid"));
				account.setCustomerId(results.getInt("customerid"));
				account.setAcctType(results.getString("type"));
				account.setStatus(results.getString("status"));
				account.setBalance(results.getFloat("balance"));
				account.setOpeningDate(results.getDate("openDate"));
			}
			if (account == null) {
				throw new BusinessException(
						"Account is not present. Cannot complete transaction." + "\nTry again.");
			}
		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException(
					"E-A04 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return account;
	}

	@Override
	public boolean isAccountPresent(int accountNumber) throws BusinessException {

		/*
		 * accountCount- based on query should be 1 if account found and zero account not found.
		 */
		int accountCount = 0;

		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "select count(accountid) from bank_schema.account where accountid = ?";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			// Account table
			preparedStatement.setInt(1, accountNumber);
			ResultSet results = preparedStatement.executeQuery();

			while (results.next()) {
				accountCount = results.getInt("count");
			}
		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException(
					"E-A05 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}

		if (accountCount >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int depositWithdrawTransaction(int accountId, float accountBalance, float transAmt,
			String transactionType) throws BusinessException {

		// results of query
		int update1 = 0;
		int update2 = 0;

		Connection conn = null;
		try {
			conn = PostgresConnection.getConnection();

			conn.setAutoCommit(false);
			int ttype = 0;// integer representation of transaction type
			if (transactionType.equals("deposit")) {
				ttype = 1;
			} else {
				ttype = 2;
			}

			String sql = "update bank_schema.account set balance = ? where accountid = ? ";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			// Account table
			preparedStatement.setFloat(1, accountBalance);
			preparedStatement.setInt(2, accountId);
			update1 = preparedStatement.executeUpdate();

			String sql2 = "insert into bank_schema.transactions "
					+ "(accountid, transactiontypeid, destinationaccount, transactiondate, amount) "
					+ "values(?,?,?, ?,?)";

			PreparedStatement preparedStatement1 = conn.prepareStatement(sql2);

			// Transactions table
			preparedStatement1.setInt(1, accountId);
			preparedStatement1.setInt(2, ttype);
			preparedStatement1.setInt(3, accountId);
			preparedStatement1.setTimestamp(4, new Timestamp(new Date().getTime()));
			preparedStatement1.setFloat(5, transAmt);
			update2 = preparedStatement1.executeUpdate();
			
			conn.commit();
			conn.close();
			
			
		} catch (ClassNotFoundException | SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new BusinessException(
						"E-A06 : Internal Error- Please contact System Administrator" + "Reason: "
								+ e1.getMessage());
			}

			throw new BusinessException(
					"E-A06 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return update1 + update2;
	}

	@Override
	public int transferToTransaction(BankAccount sourceAccount, BankAccount destinationAccount,
			float transferAmt) throws BusinessException {

		// Stores the result of the queries
		int update1 = 0;
		int update2 = 0;
		int update3 = 0;
		int update4 = 0;

		Connection conn = null;
		try {
			conn = PostgresConnection.getConnection();
			conn.setAutoCommit(false);
			
			// Updating the 'account' table.
			// Step 1 -Updating the acct of the transferor.
			String sql = "update bank_schema.account set balance = ? where accountid = ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			preparedStatement.setFloat(1, sourceAccount.getBalance());
			preparedStatement.setInt(2, sourceAccount.getAccountId());			
			update1 = preparedStatement.executeUpdate();

			// Step 2 -Updating the acct of the transferee.
			sql = "update bank_schema.account " + "set balance = ? where accountid= ?";
			PreparedStatement preparedStatement2 = conn.prepareStatement(sql);

			preparedStatement2.setFloat(1, destinationAccount.getBalance());
			preparedStatement2.setInt(2, destinationAccount.getAccountId());
			update2 = preparedStatement2.executeUpdate();

			// updating the transactions table.
			// Step 3 -Creating the transaction for the transferor
			sql = "insert into bank_schema.transactions"
					+ "(accountid, transactiontypeid, destinationaccount,transactiondate, amount) "
					+ "values(?,?,?, ?,?) ";
			PreparedStatement preparedStatement3 = conn.prepareStatement(sql);

			preparedStatement3.setInt(1, sourceAccount.getAccountId());

			// 3 transfertypeid for 'transfer to'.
			preparedStatement3.setInt(2, 3);
			preparedStatement3.setInt(3, destinationAccount.getAccountId());
			preparedStatement3.setTimestamp(4, new Timestamp(new Date().getTime()));
			preparedStatement3.setFloat(5, transferAmt);
			update3 = preparedStatement3.executeUpdate();

			// Step 4 -Creating the transaction for the transferee
			sql = "insert into bank_schema.transactions"
					+ "(accountid, transactiontypeid, destinationaccount,transactiondate, amount) "
					+ "values(?,?,?, ?,?) ";
			PreparedStatement preparedStatement4 = conn.prepareStatement(sql);

			preparedStatement4.setInt(1, destinationAccount.getAccountId());

			// 4 transfertypeid for 'transfer to'.
			preparedStatement4.setInt(2, 4);
			preparedStatement4.setInt(3, sourceAccount.getAccountId());
			preparedStatement4.setTimestamp(4, new Timestamp(new Date().getTime()));
			preparedStatement4.setFloat(5, transferAmt);
			update4 = preparedStatement4.executeUpdate();

			// Step 5 -Perform query and Commit to perform the transaction.

			conn.commit();
			conn.close();// close the connection

		} catch (ClassNotFoundException | SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {

				throw new BusinessException(
						"E-A07 : Internal Error- Please contact System Administrator" + "Reason: "
								+ e1.getMessage());
			}
			throw new BusinessException(
					"E-A07 : Internal Error- Please contact System Administrator" + "Reason: "
							+ e.getMessage());
		}

		return update1 + update2 + update3 + update4;
	}

	@Override
	public int updateAccountStausbyId(int accountId, String status) throws BusinessException {
		int update = 0;
		try (Connection conn = PostgresConnection.getConnection()) {

			// Updating the 'account' table.
			int accountStatusId;
			if (status.equals("active")) {
				accountStatusId = 1;
			} else if (status.equals("rejected")) {
				accountStatusId = 3;
			} else {
				accountStatusId = 2;
			}
			String sql = "update bank_schema.account set statusid = ? where accountid = ? ";
			PreparedStatement preparedStatement1 = conn.prepareStatement(sql);

			preparedStatement1.setFloat(1, accountStatusId);
			preparedStatement1.setInt(2, accountId);
			update = preparedStatement1.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException(
					"E-A08 : Internal Error- Please contact System Administrator" + "\nReason:: "
							+ e.getMessage());
		}
		return update;
	}

	@Override
	public int createAccountRecord(BankAccount newAccount) throws BusinessException {

		int update = 0;

		try (Connection conn = PostgresConnection.getConnection()) {
			String sql3 = "insert into bank_schema.account (customerid, accounttypeid, statusid, balance, "
					+ "opendate) values(?,"
					+ "(select accounttypeid from bank_schema.accounttype where type = ?), "
					+ "(select statusid from bank_schema.accountstatus where status =?)," + "?,?) ";
			PreparedStatement preparedStatement = conn.prepareStatement(sql3);
			preparedStatement.setInt(1, newAccount.getCustomerId());
			preparedStatement.setString(2, newAccount.getAcctType());
			preparedStatement.setString(3, newAccount.getStatus());
			preparedStatement.setFloat(4, newAccount.getBalance());
			preparedStatement.setDate(5, new java.sql.Date(newAccount.getOpeningDate().getTime()));
			update = preparedStatement.executeUpdate();
			

		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException(
					"E-A09 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return update;
	}	
}
