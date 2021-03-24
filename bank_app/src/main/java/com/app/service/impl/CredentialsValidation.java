package com.app.service.impl;

public class CredentialsValidation {
	
	public static boolean isValidUsername(String user) {
		return (user != null && user.matches("^[a-zA-Z0-9]([._-]|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$")) ? true : false;
	}
	public static boolean isValidPassword(String pass) {
		return (pass != null && pass.matches("([!@#$%^&*._-]|[a-zA-Z0-9]){4,30}")) ? true : false;
	}


}
