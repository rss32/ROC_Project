/*
 * This class prints formatted text to the console for the 'BankAccount' class.
 */

package com.app.util.displayutil;

import java.util.List;
import org.apache.log4j.Logger;

import com.app.model.BankAccount;
import com.app.model.Transaction;

public class AccountInfoDisplay {
	private static Logger log = Logger.getLogger(AccountInfoDisplay.class);
	private static final int screenWidth = 75;
	private AccountInfoDisplay() {
	}

	public static void displaySimpleActiveBankAccountTable(List<BankAccount> accounts) {
		int menuNumber = 1;
		StringBuilder sb = new StringBuilder();
		// String format
		String format = "%-4s %-11s %-14s %-13s       %-20s ";
		String format2 = "%-4d %-11d %-14s %-13s  %13.2f ";
		sb.append(String.format(format, "No.", "Account #", "Opening date", "Account type",
				"Balance"));

		sb.append("\n");
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n");

		for (BankAccount acct : accounts) {
			sb.append(String.format(format2, menuNumber, acct.getAccountId(),
					acct.getOpeningDate().toString(), acct.getAcctType().toUpperCase(),
					acct.getBalance()));
			sb.append("\n");
			menuNumber++;

		}

		log.info(sb.toString());
	}

	public static void displaySimplePendingBankAccountTable(List<BankAccount> accounts) {

		StringBuilder sb = new StringBuilder();
		// String format
		String format = "     %-11s %-14s %-13s       %-20s ";
		String format2 = "     %-11d %-14s %-13s  %13.2f ";
		sb.append(String.format(format, "Account #", "Applic. date", "Account type", "Balance"));

		sb.append("\n");
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n");

		for (BankAccount acct : accounts) {
			sb.append(String.format(format2, acct.getAccountId(), acct.getOpeningDate().toString(),
					acct.getAcctType().toUpperCase(), acct.getBalance()));
			sb.append("\n");

		}

		log.info(sb.toString());
	}

	public static void displaySimpleRejectedBankAccountTable(List<BankAccount> accounts) {

		StringBuilder sb = new StringBuilder();
		// String format
		String format = "     %-11s %-14s %-13s";
		String format2 = "     %-11d %-14s %-13s";
		sb.append(String.format(format, "Account #", "Applic. date", "Account type"));

		sb.append("\n");
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n");

		for (BankAccount acct : accounts) {
			sb.append(String.format(format2, acct.getAccountId(), acct.getOpeningDate().toString(),
					acct.getAcctType().toUpperCase()));
			sb.append("\n");

		}

		log.info(sb.toString());
	}

	public static void displayPendingAccountTable(List<BankAccount> accounts) {
		int menuNumber = 1;
		StringBuilder sb = new StringBuilder();
		// String format
		String format = "%-4s %-12s %-13s %-11s %-15s %-20s ";
		String format2 = "%-4d %-12d %-13s %-11d %-15s %13.2f ";
		sb.append(String.format(format, "No.", "Customerid", "Account type", "Account #",
				"Opening date", "Balance"));
		sb.append("\n");
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n");

		for (BankAccount acct : accounts) {
			sb.append(String.format(format2, menuNumber, acct.getCustomerId(),
					acct.getAcctType().toUpperCase(), acct.getAccountId(),
					acct.getOpeningDate().toString(), acct.getBalance()));
			sb.append("\n");
			menuNumber++;

		}

		log.info(sb.toString());
	}

	public static void displayBankAccountTable(List<BankAccount> accounts) {
		int menuNumber = 1;
		StringBuilder sb = new StringBuilder();
		// String format
		String format = "%-4s %-11s %-13s %-15s %-14s %-20s ";
		String format2 = "%-4d %-11d %-13s %-15s %-14s %13.2f ";
		sb.append(String.format(format, "No.", "Account #", "Account type", "Account Status",
				"Opening date", "Balance"));
		sb.append("\n");
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n");

		for (BankAccount acct : accounts) {
			sb.append(String.format(format2, menuNumber, acct.getAccountId(),
					acct.getAcctType().toUpperCase(), acct.getStatus(),
					acct.getOpeningDate().toString(), acct.getBalance()));
			sb.append("\n");
			menuNumber++;

		}

		log.info(sb.toString());

	}

	public static void displayAccountTransactionsTable(List<Transaction> transactionList) {

		StringBuilder sb = new StringBuilder();
		// String format
		String format = "%-3s %-22s %-12s %-12s %-13s %-8s ";
		String format2 = "%-3d %-22s %12d %12d %-13s %8.2f ";
		sb.append(String.format(format, "No.", "Transaction Date", "Src. Acct #", "Dest. Acct #",
				"Type", "Amount"));
		sb.append("\n");
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n");

		for (Transaction transaction : transactionList) {
			sb.append(String.format(format2, transaction.getTransactionId(),
					transaction.getTransactionDate().toString(), transaction.getOriginAccount(),
					transaction.getDestinationAccount(), transaction.getTransactionType(),
					transaction.getTransactionAmt()));
			sb.append("\n");

		}

		log.info(sb.toString());

	}

}
