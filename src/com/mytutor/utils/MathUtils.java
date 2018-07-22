package com.mytutor.utils;

import java.util.Random;

class MathUtils {

	private MathUtils() {

	}

	static int generateRandomNumber(int max) {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(max) + 1;
	}

}
