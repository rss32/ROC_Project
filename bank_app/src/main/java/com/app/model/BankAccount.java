package com.app.model;

import java.util.Date;

public class BankAccount {
	private int accountId,customerId;
	private String acctType, status;
	private float balance;
	Date openingDate;
	
	public BankAccount() {	
		balance = 0.0f;
		openingDate = new Date();
		status = "pending";
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public float getBalance() {
		return balance;
	}

	public void setBalance(float balance) {
		this.balance = balance;
	}

	public Date getOpeningDate() {
		return openingDate;
	}
	
	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	@Override
	public String toString() {
		return "BankAccount [accountId=" + accountId + ", customerId="
				+ customerId + ", acctType=" + acctType + ", status=" + status
				+ ", balance=" + balance + ", openingDate=" + openingDate + "]";
	}
}
