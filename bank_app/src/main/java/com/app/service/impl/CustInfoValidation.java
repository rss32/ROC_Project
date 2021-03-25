package com.app.service.impl;

public class CustInfoValidation {

	public static boolean isValidCustomerName(String name) {
		return (name != null && name.matches("^[a-zA-Z]{1}[a-zA-Z-]{2,34}")) ? true : false;
	}

	public static boolean isTaxIdValid(String ssn) {
		return (ssn != null && ssn.matches("[0-9]{9}")) ? true : false;
	}

	public static boolean isValidContactNumber(String phNum) {
		return (phNum != null && phNum.matches("[0-9]{10}")) ? true : false;
	}

	public static boolean isValidCustomerId(int id) {
		return (id >= 100000000 && id <= 599999999) ? true : false;
	}

	public static boolean isValidEmail(String email) {
		return (email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) ? true
				: false;
	}

	public static boolean isValidAge(int age) {
		int maxAge = 125;
		int minAge = 6;
		return (age > minAge && age < maxAge) ? true : false;
	}

	public static boolean isValidZip(String zip) {
		return (zip != null && zip.matches("[0-9]{5}")) ? true : false;
	}

	public static boolean isValidCity(String city) {
		return (city != null && city.matches("^[A-Za-z]{1,}[ a-zA-Z]{0,}")) ? true : false;
	}

	public static boolean isValidStreetName(String streetName) {
		return (streetName != null && streetName.matches("^[0-9A-Za-z]{1,}[ 0-9a-zA-Z]{1,}")) ? true : false;
	}

}
