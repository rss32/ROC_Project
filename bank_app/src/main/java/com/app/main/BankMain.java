/*
 * TODO RESOLVE-parts of final source code commented
 */

package com.app.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.app.exception.BusinessException;
import com.app.model.BankAccount;
import com.app.model.Credentials;
import com.app.model.Customer;
import com.app.model.Employee;
import com.app.model.Transaction;
import com.app.service.BankAccountService;
import com.app.service.CredentialsService;
import com.app.service.CustomerService;
import com.app.service.EmployeeService;
import com.app.service.impl.BankAccountServiceImpl;
import com.app.service.impl.CredentialsServiceImpl;
import com.app.service.impl.CustomerServiceImpl;
import com.app.service.impl.EmployeeServiceImpl;
import com.app.util.displayutil.AccountInfoDisplay;
import com.app.util.displayutil.ConsoleDisplay;
import com.app.util.displayutil.ConsoleInputUtil;

public class BankMain {

	private static Logger log = Logger.getLogger(BankMain.class);

	public static void main(String[] args) {

		int option = 0;
		do {
			option = displayWelcomePage();

			switch (option) {

			case 1:
				displayCustomerPortal();
				break;

			case 2:
				displayEmployeePortal();
				break;

			case 3:
				addNewCustomerAcct();
				break;

			default:
				log.info("Goodbye  ( ^_^)/");
			}
		} while (option != 4);

	}

	/*
	 * Bank User Methods ------------------------------------------------------
	 */
	// First Page
	public static int displayWelcomePage() {

		ConsoleDisplay.displayHeader("Welcome Page", "U");
		ConsoleDisplay.displayMessage("At JDBC Bank we promise to take care of YOUR money (^_^)");
		ConsoleDisplay.displayMenu("1. Customer login", "2. Employee login",
				"3. Create new customer acct", "4. Exit");
		ConsoleDisplay.displayPrompt("Please enter menu selection [1-4]");

		int userOption = 0; // stores number from user input
		boolean validInput = false; // true if user enters valid input

		do {
			try {
				userOption = Integer.parseInt(ConsoleInputUtil.readInput());
				if (userOption > 0 && userOption < 5) {
					validInput = true;
				} else {
					log.info("Please select ONLY options 1-4");
				}
			} catch (NumberFormatException e) {
				log.warn("Invalid input. Please select ONLY options 1-4");
			}
		} while (!validInput);

		return userOption;
	}

	// login display page --gets user name and password from user
	public static Credentials displayLoginPage(char userType) {

		if (userType == 'c') {
			ConsoleDisplay.displayHeader("Customer Login", "C");
		} else {
			ConsoleDisplay.displayHeader("Employee Login", "E");
		}

		ConsoleDisplay.displayPrompt("Enter username");

		String user = ConsoleInputUtil.readInput();
		ConsoleDisplay.displayPrompt("Enter password");
		String pass = ConsoleInputUtil.readInput();

		Credentials cr = new Credentials(user, pass);

		return cr;
	}

	public static void addNewCustomerAcct() {
		Customer customer = new Customer();
		Credentials credentials = new Credentials();
		CustomerService custService = new CustomerServiceImpl();
		CredentialsService credService = new CredentialsServiceImpl();

		boolean exit = false;
		boolean validInput = false;

		do {
			ConsoleDisplay.displayHeader("Create New Customer Account", "U");
			ConsoleDisplay.displayMessage("Please enter your details below");

			ConsoleDisplay.displayPrompt("First Name");
			customer.setfName(ConsoleInputUtil.readInput());

			ConsoleDisplay.displayPrompt("Last Name");
			customer.setlName(ConsoleInputUtil.readInput());

			ConsoleDisplay.displayPrompt("Date of Birth (Format: mm/dd/yyyy)");
			boolean validDateFormat = false;
			do {
				try {
					customer.setDob(
							new SimpleDateFormat("MM/dd/yyyy").parse(ConsoleInputUtil.readInput()));
					validDateFormat = true;
				} catch (ParseException e) {
					log.warn("Incorrect format. Please enter birthday again");
				}
			} while (!validDateFormat);

			ConsoleDisplay.displayPrompt(
					"Social Security Number (Format : 9 digits without dashes or spaces)");
			customer.setSsn(ConsoleInputUtil.readInput());

			ConsoleDisplay.displayPrompt("Email");
			customer.setEmail(ConsoleInputUtil.readInput());

			ConsoleDisplay.displayPrompt(
					"Contact Number (Format: 10 digits without dashes, splaces or brackets)");
			customer.setContactNum(ConsoleInputUtil.readInput());

			// address data
			ConsoleDisplay.displayPrompt("Street Address");
			customer.setStreetName(ConsoleInputUtil.readInput());

			ConsoleDisplay.displayPrompt("City");
			customer.setCity(ConsoleInputUtil.readInput());

			ConsoleDisplay.displayPrompt("Zip (Format : 5 digits)");
			customer.setZip(ConsoleInputUtil.readInput());

			try {
				validInput = custService.validateCustomerInfo(customer);

			} catch (BusinessException e) {
				log.warn(e.getMessage());
				log.info("Please try again."
						+ " \nEnter '[e]' if you want to exit or hit press the 'Enter' key"
						+ "\non you keyboard to try again");
				if (ConsoleInputUtil.readInput().equals("[e]")) {
					exit = true;
				}
			}
		} while (!exit && !validInput);

		if (!exit) {
			ConsoleDisplay.displayMessage(
					"\nNow that we have your personal details let's secure your account.");
			validInput = false;
			do {
				ConsoleDisplay
						.displayPrompt("Create new username (must be at least 5 characters long)");
				credentials.setUsername(ConsoleInputUtil.readInput());

				ConsoleDisplay
						.displayPrompt("Create new password (must be at least 4 characters long)");
				credentials.setPassword(ConsoleInputUtil.readInput());

				try {
					validInput = credService.validateCredentialsInfo(credentials);
				} catch (BusinessException e) {
					log.warn(e.getMessage());
				}
			} while (!validInput);

			customer.setCredentials(credentials);

			ConsoleDisplay.displayMessage("\nFinal step, create your bank account.");

			customer.addBankAccount(createNewBankAccount());

			try {
				custService.createCustomer(customer);
			} catch (BusinessException e) {
				log.warn(e.getMessage());
			}
		}
	}

	/*
	 * Bank Customer Methods---------------------------------------------------
	 */
	public static void displayCustomerPortal() {

		Customer customer = null;
		CustomerService custService = new CustomerServiceImpl();

		boolean loginSuccessful = false;
		int attemptsRemaining = 3; // number of times that a user can login
		do {
			Credentials cred = displayLoginPage('c');

			try {
				customer = custService.getCustomerByCredentials(cred);
				loginSuccessful = true;
			} catch (BusinessException e) {
				log.warn(
						e.getMessage() + "\nLogin attempts remaining: " + (attemptsRemaining - 1));
				attemptsRemaining--;
			}
		} while (!loginSuccessful && attemptsRemaining != 0);

		if (loginSuccessful) {

			int option = 0;
			do {
				option = displayCustomerAcctOptions(customer.getfName(), customer.getlName(),
						customer.getId());

				switch (option) {

				case 1:
					displayCustomerAccounts(customer.getId());
					break;

				case 2:
					customerApplyForBankAccount(customer);
					break;

				case 3: // log.info("Feature coming soon :)");

				default:
					log.info("Feature coming soon :)");

				}

			} while (option < 4);
		}
	}

	/*
	 * Presents a list of options for the customer to view accts or update information.
	 */
	public static int displayCustomerAcctOptions(String fName, String lName, int id) {

		ConsoleDisplay.displayHeader(" Customer Account Options", "C");
		ConsoleDisplay
				.displayMessage("Hello " + fName + " " + lName + " [Customer no. : " + id + "]");

		ConsoleDisplay.displayMenu("1. View accounts", "2. Add new bank account",
				"3. Update personal information", "4. Exit");
		ConsoleDisplay.displayPrompt("Please enter menu selection [1-4]");

		int userOption = 0; // stores number from user input
		boolean validInput = false; // true if user enters valid input

		do {
			try {
				userOption = Integer.parseInt(ConsoleInputUtil.readInput());
				if (userOption > 0 && userOption < 5) {
					validInput = true;
				} else {
					log.info("Please select ONLY options 1-4");
				}
			} catch (NumberFormatException e) {
				log.warn("Invalid input. Please select ONLY options 1-4");
			}
		} while (!validInput);

		return userOption;
	}

	public static void displayCustomerAccounts(int customerId) {

		BankAccountService bankAccountService = new BankAccountServiceImpl();

		List<BankAccount> baList = new ArrayList<>();
		List<BankAccount> activeAccounts = null;
		List<BankAccount> pendingAccounts = null;
		List<BankAccount> rejectedAccounts = null;

		boolean exitPage = false;// exit the view account page
		do {

			try {
				baList = bankAccountService.getBankAccounts(customerId);
			} catch (BusinessException e) {

				log.warn(e.getMessage());
			}

			activeAccounts = bankAccountService.filterActiveAccounts(baList);
			pendingAccounts = bankAccountService.filterPendingAccounts(baList);
			rejectedAccounts = bankAccountService.filterRejectedAccounts(baList);

			ConsoleDisplay.displayHeader("View Accounts", "C");

			if (activeAccounts.size() > 0) {
				ConsoleDisplay.displayMessage(" Active Accounts");

				AccountInfoDisplay.displaySimpleActiveBankAccountTable(activeAccounts);
			}

			if (pendingAccounts.size() > 0) {
				ConsoleDisplay.displayMessage("\n Accounts Pending Approval");
				AccountInfoDisplay.displaySimplePendingBankAccountTable(pendingAccounts);
			}
			if (rejectedAccounts.size() > 0) {

				ConsoleDisplay.displayMessage("\n Declined Accounts");
				AccountInfoDisplay.displaySimpleRejectedBankAccountTable(rejectedAccounts);
			}
			if (activeAccounts.size() > 1) {
				ConsoleDisplay.displayPromptWithLine(
						"Select account to perform transaction [1-" + activeAccounts.size()
								+ "] or '" + (activeAccounts.size() + 1) + "' to exit");
			} else if (activeAccounts.size() == 1) {
				ConsoleDisplay
						.displayPromptWithLine("Enter '1' to perform transaction or '2' to exit");
			} else {
				ConsoleDisplay.displayPromptWithLine("Enter '1' to exit");
			}

			int userOption = 0; // stores number from user input
			boolean validInput = false; // true if user enters valid input

			do {
				try {
					userOption = Integer.parseInt(ConsoleInputUtil.readInput());
					if (userOption > 0 && userOption <= (activeAccounts.size())) {
						validInput = true;

						int option = 0;
						do {

							option = displayTransactionScreen(activeAccounts.get(userOption - 1));

							switch (option) {
							case 1:

								// let us return an account
								activeAccounts.set((userOption - 1),
										displayDepositScreen(activeAccounts.get(userOption - 1)));
								break;
							case 2:
								activeAccounts.set((userOption - 1), displayWithdrawalScreen(
										activeAccounts.get(userOption - 1)));
								break;
							case 3:
								activeAccounts.set((userOption - 1),
										displayTransferScreen(activeAccounts.get(userOption - 1)));
								break;
							default:
							}

						} while (option < 4); // exit condition for transaction
												// page

					} else if (userOption == (activeAccounts.size() + 1)) {
						validInput = true;
						exitPage = true;
					} else {
						log.info("Please select ONLY options 1-" + (activeAccounts.size() + 1));
					}
				} catch (NumberFormatException e) {
					log.warn("Invalid input. Only numbers are allowed.Try Again");
				}
			} while (!validInput);
		} while (!exitPage);

	}

	public static void customerApplyForBankAccount(Customer customer) {

		BankAccountService bankAccountService = new BankAccountServiceImpl();

		BankAccount ba = createNewBankAccount();

		try {
			ba.setCustomerId((customer.getId()));
			bankAccountService.createBankAccount(ba);
		} catch (BusinessException e) {
			log.warn(e.getMessage());
		}
	}

	public static BankAccount createNewBankAccount() {
		BankAccount ba = new BankAccount();
		BankAccountService baService = new BankAccountServiceImpl();
		ConsoleDisplay.displayHeader("Apply for Bank Account", "U");
		ConsoleDisplay.displayMessage("Please enter your details below");
		boolean validInput = false;
		do {
			ConsoleDisplay.displayPrompt("What type of account do you want to open ?\n"
					+ "Please enter only 'checking' or 'savings'.");
			ba.setAcctType(ConsoleInputUtil.readInput());

			ConsoleDisplay.displayPrompt("How much money do you want to deposit ?");

			try {
				ba.setBalance(Float.parseFloat(ConsoleInputUtil.readInput()));
				try {
					validInput = baService.validateNewBankAccountInfo(ba);
				} catch (BusinessException e) {
					log.warn(e.getMessage());
					log.info("Try again.");
				}

			} catch (NumberFormatException e) {
				log.warn("Please enter a number");
			}

		} while (!validInput);

		ConsoleDisplay
				.displayMessage("\n" + "Thank you for setting up an account JDBC Bank. *(^o^)*"
						+ "\nYour account is in the approval procces. You cannot"
						+ "\nperform transactions on this account.");

		ConsoleDisplay.displayPrompt("\nPress 'Enter' to exit");
		ConsoleInputUtil.readInput();

		return ba;
	}

	// TODO insert update user info

	public static int displayTransactionScreen(BankAccount account) {

		ConsoleDisplay.displayHeader("Transactions on Account", "C");
		ConsoleDisplay.displayMessage(account.getAcctType().toUpperCase() + ">>  " + "Account No.: "
				+ account.getAccountId()
				+ String.format("     >> Balance: $ %.2f", account.getBalance()));

		ConsoleDisplay.displayMenu("1. Deposit", "2. Withdraw", "3. Transfer to another account",
				"4. Exit");
		ConsoleDisplay.displayPrompt("Please enter menu selection [1-4]");

		int userOption = 0; // stores number from user input
		boolean validInput = false; // true if user enters valid input

		do {
			try {
				userOption = Integer.parseInt(ConsoleInputUtil.readInput());
				if (userOption > 0 && userOption < 5) {
					validInput = true;
				} else {
					log.info("Please select ONLY options 1-4");
				}
			} catch (NumberFormatException e) {
				log.warn("Invalid input. Please select ONLY options 1-4");
			}
		} while (!validInput);

		return userOption;

	}

	public static BankAccount displayDepositScreen(BankAccount account) {

		BankAccountService bankAccountService = new BankAccountServiceImpl();

		ConsoleDisplay.displayHeader("Deposit", "C");
		ConsoleDisplay.displayMessage(account.getAcctType().toUpperCase() + ">>  " + "Account No.: "
				+ account.getAccountId()
				+ String.format("     >> Balance: $ %.2f", account.getBalance()));
		ConsoleDisplay.displayPrompt("Please enter deposit amount");

		float depositAmt = 0.00f; // stores number from user input
		boolean validInput = false; // true if user enters valid input

		do {
			try {
				depositAmt = Float.parseFloat(ConsoleInputUtil.readInput());
				account = bankAccountService.depositTransaction(account, depositAmt);
				validInput = true;
			} catch (NumberFormatException e) {
				log.warn("Invalid input. Only numbers allowed. Try again.");
			} catch (BusinessException e) {
				log.warn(e.getMessage());
			}
		} while (!validInput);
		ConsoleDisplay.displayMessage(
				"Deposit of " + String.format("%.2f $", depositAmt) + " successful *(^o^)*");

		ConsoleDisplay.displayPrompt("\nPress 'Enter' to continue.");
		ConsoleInputUtil.readInput();
		return account;
	}

	public static BankAccount displayWithdrawalScreen(BankAccount account) {

		BankAccountService bankAccountService = new BankAccountServiceImpl();

		ConsoleDisplay.displayHeader("Withdrawal", "C");
		ConsoleDisplay.displayMessage(account.getAcctType().toUpperCase() + ">>  " + "Account No.: "
				+ account.getAccountId()
				+ String.format("     >> Balance: $ %.2f", account.getBalance()));
		ConsoleDisplay.displayPrompt("Please enter withdrawal amount");

		float withdrawAmt = 0.00f; // stores number from user input
		boolean validInput = false; // true if user enters valid input

		do {
			try {
				withdrawAmt = Float.parseFloat(ConsoleInputUtil.readInput());
				account = bankAccountService.withdrawalTransaction(account, withdrawAmt);
				validInput = true;
			} catch (NumberFormatException e) {
				log.warn("Invalid input. Only numbers allowed. Try Again.");
			} catch (BusinessException e) {
				log.warn(e.getMessage());
			}
		} while (!validInput);
		ConsoleDisplay.displayMessage(
				"Withdrawal of " + String.format("%.2f $", withdrawAmt) + " successful *(^o^)*");

		ConsoleDisplay.displayPrompt("\nPress 'Enter' to continue.");
		ConsoleInputUtil.readInput();
		return account;
	}

	public static BankAccount displayTransferScreen(BankAccount account) {

		BankAccountService bankAccountService = new BankAccountServiceImpl();

		ConsoleDisplay.displayHeader("Transfer to Another Account", "C");
		ConsoleDisplay.displayMessage(account.getAcctType().toUpperCase() + ">>  " + "Account No.: "
				+ account.getAccountId()
				+ String.format("     >> Balance: $ %.2f", account.getBalance()));

		float transferAmt = 0.00f; // stores number from user input
		int transferAcctNum = 0; // stores the account to transfer the money
		boolean validInput = false; // true if user enters valid input
		boolean stopDisplayTransferScreen = false;
		// String inputString;// stores the input from the user

		do {

			// getting the amt to be transferred

			/*
			 * in the case that a wrong/invalid input is made we make sure that 'valid input' is
			 * always false before getting input.
			 */
			validInput = false;
			ConsoleDisplay.displayPrompt("Please enter transfer amount");
			do {

				try {
					transferAmt = Float.parseFloat(ConsoleInputUtil.readInput());
					validInput = true;
				} catch (NumberFormatException e) {
					log.warn("Invalid input. Only numbers allowed. Try again.");
				}

			} while (!validInput);

			/*
			 * (if statement)don't execute anything else if the user wants to quit.
			 */
			if (!stopDisplayTransferScreen) {

				// getting the transfer account.
				ConsoleDisplay.displayPrompt(
						"Please enter the seven (7) digit transfer destination account. \n"
								+ "Use format: ####### ");

				validInput = false;
				do {
					try {
						transferAcctNum = Integer.parseInt(ConsoleInputUtil.readInput());
						validInput = true;

					} catch (NumberFormatException e) {
						log.warn("Invalid input. Only numbers allowed. Try again.");
					}
				} while (!validInput);

				try {
					account = bankAccountService.transferToTransaction(account, transferAcctNum,
							transferAmt);

					ConsoleDisplay.displayMessage("Transfer of "
							+ String.format("%.2f $", transferAmt) + " successful *(^o^)*");
					stopDisplayTransferScreen = true;

				} catch (BusinessException e) {

					/*
					 * I'm giving the user to abruptly exit this screen if he/she does not want to
					 * continue with transaction.
					 */
					log.warn(e.getMessage());
					log.info("Please try again."
							+ " \nEnter '[e]' if you want to exit or hit press the 'Enter' key"
							+ "\non you keyboard to try again.");

					if (ConsoleInputUtil.readInput().equals("[e]")) {
						stopDisplayTransferScreen = true;
					}
				} catch (Exception e) {
					log.warn(e.getMessage());
				}
			}
		} while (!stopDisplayTransferScreen);
		ConsoleDisplay.displayPrompt("Press 'Enter' to continue.");
		ConsoleInputUtil.readInput();

		return account;

	}

	/*
	 * Bank Employee Methods---------------------------------------------------
	 */

	public static void displayEmployeePortal() {

		Employee employee = null;
		EmployeeService employeeService = new EmployeeServiceImpl();
		BankAccountService bankAccountService = new BankAccountServiceImpl();

		boolean loginSuccessful = false;
		int attemptsRemaining = 3; // number of times that a user can login
		do {
			Credentials cred = displayLoginPage('e');

			try {
				employee = employeeService.getEmployeeByCredentials(cred);
				loginSuccessful = true;
			} catch (BusinessException e) {
				log.warn(
						e.getMessage() + "\nLogin attempts remaining: " + (attemptsRemaining - 1));
				attemptsRemaining--;
			}

		} while (!loginSuccessful && attemptsRemaining != 0);

		if (loginSuccessful) {
			log.info("log in sucessful");

			int option = 0;
			do {
				option = displayEmployeeAcctOptions(employee.getfName(), employee.getlName(),
						employee.getId());

				switch (option) {

				case 1:
					try {
						List<BankAccount> pendingBankaccountList = bankAccountService
								.getPendingBankAccounts();
						displayPendingCustomerAccounts(pendingBankaccountList);

					} catch (BusinessException e) {
						log.warn(e.getMessage());
						ConsoleDisplay.displayPrompt("\nPress 'Enter' to exit");
						ConsoleInputUtil.readInput();
					}
					break;
				case 2:
					displayCustomerBankAccountDetails();
					break;

				case 3:
					displayAcountTransactionDetails();
				}
			} while (option < 4);
		}
	}

	public static int displayEmployeeAcctOptions(String fName, String lName, int id) {

		ConsoleDisplay.displayHeader(" Employee  Options", "E");
		ConsoleDisplay
				.displayMessage("Hello " + fName + " " + lName + " [Employee no. : " + id + "]");

		ConsoleDisplay.displayMenu("1. View accounts for approval",
				"2. View a customer's bank account details", "3. See all transactions", "4. Exit");
		ConsoleDisplay.displayPrompt("Please enter menu selection [1-4]");

		int userOption = 0; // stores number from user input
		boolean validInput = false; // true if user enters valid input

		do {
			try {
				userOption = Integer.parseInt(ConsoleInputUtil.readInput());
				if (userOption > 0 && userOption < 5) {
					validInput = true;
				} else {
					log.info("Please select ONLY options 1-4");
				}
			} catch (NumberFormatException e) {
				log.warn("Invalid input. Please select ONLY options 1-4");
			}
		} while (!validInput);

		return userOption;
	}

	public static void displayPendingCustomerAccounts(List<BankAccount> pendingAccountList) {
		ConsoleDisplay.displayHeader(" Employee  Options", "E");
		ConsoleDisplay.displayMessage("Please select account to change status");
		AccountInfoDisplay.displayPendingAccountTable(pendingAccountList);

		boolean validInput = false;
		int acctChoice = 0; // stores the value of the menu selection.
		int acctStatusEntry = 0;// stores the user's choice for acct status.

		do {
			ConsoleDisplay.displayPrompt("Enter Account you want to change status " + "[1-"
					+ pendingAccountList.size() + "]");

			// getting the account selection from user
			try {
				acctChoice = Integer.parseInt(ConsoleInputUtil.readInput());
				if (acctChoice >= 1 && acctChoice <= pendingAccountList.size()) {
					validInput = true;
				} else {
					log.info("Please enter between 1 and " + pendingAccountList.size() + ".");
				}

			} catch (NumberFormatException e) {
				log.warn("Not an option. Try again.");
			}
		} while (!validInput);

		// getting the status selection form user
		validInput = false;
		do {
			ConsoleDisplay.displayPrompt(
					"Enter- '1' to approve account, " + "\n       '2' to keep status unchanged, and"
							+ "\n       '3' to reject the account");
			try {
				acctStatusEntry = Integer.parseInt(ConsoleInputUtil.readInput());
				if (acctStatusEntry >= 1 && acctStatusEntry <= 3) {
					validInput = true;
				} else {
					log.info("Please enter between 1 and 3.");
				}

			} catch (NumberFormatException e) {
				log.warn("Not an option. Try again.");
			}
		} while (!validInput);

		BankAccount ba = pendingAccountList.get((acctChoice - 1));

		switch (acctStatusEntry) {
		case 1:
			ba.setStatus("active");
			break;
		case 3:
			ba.setStatus("rejected");
			break;
		}

		BankAccountService bankAccountService = new BankAccountServiceImpl();
		try {
			int updated = bankAccountService.updateBankAccountStatus(ba);
			if (updated > 0) {
				log.info("Account successfully updated");
			}
		} catch (BusinessException e) {
			log.warn(e.getMessage());
		}
		ConsoleDisplay.displayPrompt("Press 'Enter' to continue.");
		ConsoleInputUtil.readInput();
	}

	public static void displayCustomerBankAccountDetails() {

		CustomerService customerService = new CustomerServiceImpl();
		List<BankAccount> customerBankAccounts = null;
		Customer customer = null;
		ConsoleDisplay.displayMessage(" View Customer Account Details");

		boolean validInput = false;
		int acctNumber = 0; // stores the value of the account number selection.

		do {
			ConsoleDisplay.displayPrompt(
					"Please enter customer id (9 digits between 100000000 and 599999999)");

			// getting the account number from user
			try {
				acctNumber = Integer.parseInt(ConsoleInputUtil.readInput());
				if (customerService.validateCustomerId(acctNumber)) {
					validInput = true;
				}
			} catch (NumberFormatException e) {
				log.warn("Not a number. Try again.");
			} catch (BusinessException e) {
				log.info(e.getMessage() + "\nTry again.");

			}
		} while (!validInput);

		try {
			customer = customerService.getCustomerById(acctNumber);

			BankAccountService bankAccountService = new BankAccountServiceImpl();
			customerBankAccounts = bankAccountService.getBankAccounts(acctNumber);
			ConsoleDisplay.displayHeader(
					"Customer details for " + customer.getfName() + " " + customer.getlName(),
					"2.2");
			ConsoleDisplay.displayMessage("Contact Number: " + customer.getContactNum()
					+ "  Email: " + customer.getEmail() + "   Dob: " + customer.getDob().toString()
					+ "\nAddress: " + customer.getStreetName() + " " + customer.getCity() + " "
					+ customer.getZip());

			AccountInfoDisplay.displayBankAccountTable(customerBankAccounts);
			ConsoleDisplay.displayPrompt("\nPress 'Enter' to exit");
			ConsoleInputUtil.readInput();

		} catch (BusinessException e) {

			log.info(e.getMessage());
			ConsoleDisplay.displayPrompt("\nPress 'Enter' to exit");
			ConsoleInputUtil.readInput();
		}

	}

	public static void displayAcountTransactionDetails() {
		ConsoleDisplay.displayHeader(" Account Transactions", "E");
		ConsoleDisplay.displayMessage("Below is a log of all account transactions");
		List<Transaction> customerTransactions = null;

		try {

			BankAccountService bankAccountService = new BankAccountServiceImpl();
			customerTransactions = bankAccountService.getAllTransactions();

			AccountInfoDisplay.displayAccountTransactionsTable(customerTransactions);
			ConsoleDisplay.displayPrompt("\nPress 'Enter' to continue.");
			ConsoleInputUtil.readInput();

		} catch (BusinessException e) {

			log.info(e.getMessage());
			ConsoleDisplay.displayPrompt("\nPress 'Enter' to continue.");
			ConsoleInputUtil.readInput();
		}
	}

}
