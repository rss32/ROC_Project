package com.app.model;

public class Employee {
	private int id;
	private String fName, lName;
	private String email, contactNum;	
	private Credentials cr;	
	private String privilegeLevel; //Admin or regular user
	
	public Employee() {		
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

	public Credentials getCredentials() {
		return cr;
	}

	public void setCredentials(Credentials cr) {
		this.cr = cr;
	}

	public String getPrivilegeLevel() {
		return privilegeLevel;
	}
	public void setPrivilegeLevel(String privilegeLevel) {
		this.privilegeLevel= privilegeLevel;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", fName=" + fName + ", lName=" + lName + ", email=" + email
				+ ", contactNum=" + contactNum + ", cr=" + cr + ", privilegeLevel=" + privilegeLevel
				+ "]";
	}
}
