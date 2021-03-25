package com.app.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Customer {
	private int id;
	private String fName, lName;
	private String ssn, email, contactNum;
	private String streetName, city, zip;
	private Credentials cr;
	private Date dob;
	private List<BankAccount> bankAccounts;
	
	public Customer() {
		bankAccounts = new ArrayList<>();
	}		
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}	
	
	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
	public Credentials getCredentials() {
		return cr;
	}

	public void setCredentials(Credentials cr) {
		this.cr = cr;
	}
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}	

	public Credentials getCr() {
		return cr;
	}
	
	public List<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(List<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
	
	public void addBankAccount(BankAccount ba) {
		this.bankAccounts.add(ba);
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", fName=" + fName + ", lName=" + lName
				+ ", ssn=" + ssn + ", email=" + email + ", contactNum="
				+ contactNum + ", streetName=" + streetName + ", city=" + city
				+ ", zip=" + zip + ", cr=" + cr + ", dob=" + dob
				+ ", bankAccounts=" + bankAccounts + "]";
	}
}
