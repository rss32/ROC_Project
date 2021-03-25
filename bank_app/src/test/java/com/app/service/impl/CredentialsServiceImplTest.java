package com.app.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.service.CredentialsService;

class CredentialsServiceImplTest {

	private static CredentialsService service;
	private static Credentials cred, cred2;
	
	@BeforeAll
	public static void setUp() {
		service = new CredentialsServiceImpl();
		cred = new Credentials();
		cred.setUsername("user2");
		cred.setPassword("pass");
		
		cred2 = new Credentials();
		cred2.setUsername("user99");
		cred2.setPassword("99");		
	}
	
	@Test
	void testValidateCredentialsInfoWithValidInfo() {
		try {
			assertEquals(true,service.validateCredentialsInfo(cred));
		} catch (BusinessException e) {
			
			e.getStackTrace();
		}
	}

	@Test
	void testValidateCredentialsInfoWithInvalidInfo() {
		try {
			assertEquals(false,service.validateCredentialsInfo(cred2));
		} catch (BusinessException e) {
			
			e.getStackTrace();
		}
	}
}
