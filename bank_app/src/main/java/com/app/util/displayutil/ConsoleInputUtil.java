package com.app.util.displayutil;

import java.util.Scanner;

public final class ConsoleInputUtil {

	private static Scanner userInputScan;

	// Make class non-instantiatable
	private ConsoleInputUtil() {
	}

	// get input from console
	public static String readInput() {

		if (userInputScan == null) {
			userInputScan = new Scanner(System.in);
		}

		return userInputScan.nextLine();
	}
}
