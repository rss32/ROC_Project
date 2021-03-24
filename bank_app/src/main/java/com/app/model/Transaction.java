package com.app.model;

import java.sql.Timestamp;

public class Transaction {
	private int transactionId;
	private int originAccount, destinationAccount;
	private String transactionType;
	private Timestamp transactionDate;
	private float transactionAmt;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getOriginAccount() {
		return originAccount;
	}

	public void setOriginAccount(int originAccount) {
		this.originAccount = originAccount;
	}

	public int getDestinationAccount() {
		return destinationAccount;
	}

	public void setDestinationAccount(int destinationAccount) {
		this.destinationAccount = destinationAccount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public float getTransactionAmt() {
		return transactionAmt;
	}

	public void setTransactionAmt(float transactionAmt) {
		this.transactionAmt = transactionAmt;
	}
}
