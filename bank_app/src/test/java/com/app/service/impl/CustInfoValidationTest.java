package com.app.service.impl;

import static org.junit.jupiter.api.Assertions.*;

//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CustInfoValidationTest {

	
	@Test
	void testIsValidCustomerNameForNamesWithNumbers() {
		assertEquals(false,CustInfoValidation.isValidCustomerName("John1thon"));
	}
	@Test
	void testIsValidCustomerNameWithThreeLetters() {
		assertEquals(true,CustInfoValidation.isValidCustomerName("abc"));
	}


	@Test
	void testIsTaxIdValidWithoutSpaces() {
		assertEquals(true,CustInfoValidation.isTaxIdValid("456890765"));
	}
	@Test
	void testIstaxIdValidWithSpaces() {
		assertEquals(false,CustInfoValidation.isTaxIdValid("456890 765"));
	}
	@Test
	void testIstaxIdValidWithEightDigits() {
		assertEquals(false,CustInfoValidation.isTaxIdValid("45689076"));
	}
	@Test
	void testIstaxIdValidWithOneLetter() {
		assertEquals(false,CustInfoValidation.isTaxIdValid("4N6890765"));
	}

	@Test
	void testIsValidContactNumberwithTenDigits() {
		assertEquals(true, CustInfoValidation.isValidContactNumber("1234567890"));
	}
	
	@Test
	void testIsValidContactNumberwithElevenDigits() {
		assertEquals(false, CustInfoValidation.isValidContactNumber("12345678901"));
	}
	@Test
	void testIsValidContactNumberwithSpace() {
		assertEquals(false, CustInfoValidation.isValidContactNumber("99912 34567"));
	}

	@Test
	void testIsValidCustomerIdWithNineDigits() {
		assertEquals(true, CustInfoValidation.isValidCustomerId(123456789));
	}
	@Test
	void testIsValidCustomerIdEqualToOrOverSixHundredMillion() {
		assertEquals(false, CustInfoValidation.isValidCustomerId(600000000));
	}

	@Test
	void testIsValidEmailWithSingleCharacter() {
		assertEquals(true, CustInfoValidation.isValidEmail("a@h.com"));
	}
	
	@Test
	void testIsValidEmailWithDomainDotTLDDotTLD() {
		assertEquals(true, CustInfoValidation.isValidEmail("name@h.co.uk"));
	}

	@Test
	void testIsValidAge() {
		assertEquals(false, CustInfoValidation.isValidAge(125));
	}

	@Test
	void testIsValidZipWithThreeNumbers() {
		assertEquals(false, CustInfoValidation.isValidZip("125"));
	}
	@Test
	void testIsValidZipWithOneLetters() {
		assertEquals(false, CustInfoValidation.isValidZip("1251L2"));
	}
	@Test
	void testIsValidZipWithFiveDigits() {
		assertEquals(true, CustInfoValidation.isValidZip("12345"));
	}
}
