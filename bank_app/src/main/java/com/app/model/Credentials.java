package com.app.model;

public class Credentials {
	private int id;
	private String user, pass;

	public Credentials(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	public Credentials() {		
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return user;
	}

	public String getPassword() {
		return pass;
	}

	public int getId() {
		return id;
	}
	public void setUsername(String user) {
		this.user = user;
	}
	public void setPassword(String pass) {
		this.pass = pass;
	}

	@Override
	public boolean equals(Object c) {

		// self check if self
		if (this == c) {
			return true;
		}
		// check if null
		if (c == null) {
			return false;
		}
		// check if same class
		if (this.getClass() != c.getClass()) {
			return false;
		}
		Credentials cr = (Credentials) c;
		if (this.id == cr.getId() && this.user == cr.getUsername()
				&& this.pass == cr.getPassword()) {
			return true;
		}

		return false;
	}
	
	@Override
	public String toString() {
		return "Credentials [id=" + id + ", user=" + user + ", pass=" + pass
				+ "]";
	}
}
