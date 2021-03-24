package com.app.service.impl;

public class BankAccountValidation {
	
	public static boolean isNonNegativeNumber(float number) {
		return (number >= 0) ? true : false;
	}

	public static boolean isBalanceGreater(float balance, float number) {
		return (balance >= number) ? true : false;
	}
	public static boolean isAccountCorrectFormat(float acctNum) {
		return (acctNum >= 1000000 && acctNum <= 9999999) ? true : false;
	}
	
	public static boolean isCorrectAccountType(String s) {
		return (s.equals("checking")||s.equals("savings")) ? true : false;
	}

}
