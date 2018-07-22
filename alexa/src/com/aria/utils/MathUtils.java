package com.mytutor.utils;

import java.util.ArrayList;
import java.util.Random;

public class MathUtils {
	
	public static int generateRandomNumber(int max)
	{
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(max) + 1;
	}
	
	public static int generateRandomNumber(int max, ArrayList<Integer> excludeNumberList) {
		Random randomGenerator = new Random();

		int number = -1;

		do {
			number = randomGenerator.nextInt(max) + 1;
		} while (excludeNumberList.contains(number));

		return number;
	}

	public static int generateRandomNumber(int max, int excludeNumber) {
		Random randomGenerator = new Random();

		int number = -1;

		do {
			number = randomGenerator.nextInt(max) + 1;
		} while (number == excludeNumber);

		return number;
	}
	
	public static ArrayList<Integer> generateNRandomNumbersThatAddUptoTen(int n)
	{
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for(int i=1;i<=n;i++)
			numbers.add(1);
		int sumOfNumbers = sumOfNumbersInAnArray(numbers);
		ArrayList<Integer> generatedNumbers = new ArrayList<Integer>();
		while(sumOfNumbers<10)
		{
			int num = generateRandomNumber(10-sumOfNumbers);
			int index =generateRandomNumber(n)-1;
			generatedNumbers.add(index);
			numbers.set(index, numbers.get(index)+num);
			sumOfNumbers = sumOfNumbersInAnArray(numbers);
		}
		
		return numbers;
	}
	
	public static int sumOfNumbersInAnArray(ArrayList<Integer> list)
	{
		int sum=0;
		for(int i=0;i<list.size();i++)
			sum+=list.get(i);
		return sum;
	}

	public static ArrayList<Integer> genernateNRandomNumbers(int max, int n) {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		Random randomGenerator = new Random();

		for (int i = 1; i <= n; i++) {
			int num = randomGenerator.nextInt(max) + 1;
			if (numbers.size() == 0) {
				numbers.add(num);
				continue;
			}
			while(numbers.contains(num)) {
				num = randomGenerator.nextInt(max) + 1;
			} 
			numbers.add(num);
		}
		return numbers;
	}
	
	

	public static int generateRandomNumberLesserThan(int max, int seed) {
		Random randomGenerator = new Random();

		int number = -1;

		do {
			number = randomGenerator.nextInt(max) + 1;
		} while (number >= seed);

		return number;
	}

	public static int generateRandomNumberGreaterThan(int max, int seed) {
		Random randomGenerator = new Random();

		int number = -1;

		do {
			number = randomGenerator.nextInt(max) + 1;
		} while (number <= seed);

		return number;
	}

	public static int generateRandomNumber(int max, boolean even) {
		Random randomGenerator = new Random();

		int number = -1;

		if (even) {
			do {
				number = randomGenerator.nextInt(max) + 1;
			} while (number % 2 != 0);
		} else {
			do {
				number = randomGenerator.nextInt(max) + 1;
			} while (number % 2 != 1);
		}

		return number;
	}
	
	public static int generateRandomNumberForEquation(int max, boolean even, int seed) {
		Random randomGenerator = new Random();

		int number = -1;

		if (even) {
			do {
				number = randomGenerator.nextInt(max) + 1;
			} while (((number-seed) % 2 != 0) || number<seed);
		} else {
			do {
				number = randomGenerator.nextInt(max) + 1;
			} while (((number-seed) % 2 != 1) || number<seed);
		}

		return number;
	}
	
	public static boolean generateRandomBoolean()
	{
		Random randomGenerator = new Random();
		int x = randomGenerator.nextInt(2);
		if(x==0) 
			return true;
		else
			return false;
	}
	
}
