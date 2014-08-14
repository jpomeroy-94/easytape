package com.jeffreypomeroy.easytape;

public class EasytapeUtil {
	private static String userName = "guest";

	public static void setUserName(String userName) {
		EasytapeUtil.userName = userName;
	}

	public static String getUserName() {
		return userName;
	}
}
