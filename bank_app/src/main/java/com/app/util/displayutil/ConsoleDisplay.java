package com.app.util.displayutil;

import org.apache.log4j.Logger;

public class ConsoleDisplay {
	
	private static Logger log = Logger.getLogger(ConsoleDisplay.class);
	private static int screenWidth = 75;

	private ConsoleDisplay() {		
	}	

	public static void displayHeader(String text, String userType) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < screenWidth; i++) {
			sb.append("=");
		}
		sb.append("\n " + userType + " >> " + "JDBC Bank: " + text + "\n");
		for (int i = 0; i < screenWidth; i++) {
			sb.append("=");
		}

		log.info(sb.toString());
	}

	public static void displayMenu(String... menuItems) {
		StringBuilder sb = new StringBuilder();

		for (String s : menuItems) {
			log.info("     " + s);
		}
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}

		log.info(sb.toString());
	}

	public static void displayMessage(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(message + "\n");

		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n");
		log.info(sb.toString());
	}

	public static void displayPrompt(String prompt) {
		log.info(prompt + " : ");
	}

	public static void displayPromptWithLine(String prompt) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < screenWidth; i++) {
			sb.append("-");
		}
		sb.append("\n" + prompt);

		displayPrompt(sb.toString());
	}
}
