package com.mytutor.utils;

import java.util.Random;

public class MessageBuilder {

	public static String CONGRATULATIONS_AUDIO_FILE = "<audio src=\"https://s3.amazonaws.com/123math/audio_files/alexa_happykids.mp3\" />";

	private static String[] correctResponsePhrases;

	public static String[] getCorrectResponsePhrases() {
		if (correctResponsePhrases == null) {

			correctResponsePhrases = new String[] { "bazinga", "booya", "bravo", "cha ching", "yippee", "hurray",
					"bingo", "ooh la la", "ta da", "wahoo" };

		}
		return correctResponsePhrases;

	}

	public static int pickARandomArrayMember(String[] array) {
		Random randomGenerator = new Random();
		int index = randomGenerator.nextInt(array.length);

		return index;

		// return new Person(array[index], heOrSheMap.get(array[index]) );

	}

	public static int pickARandomArrayMember(String[] array, int skipIndex) {
		Random randomGenerator = new Random();
		int index = -1;
		do {
			index = randomGenerator.nextInt(array.length);
		} while (index == skipIndex);

		return index;

		// return new Person(array[index], heOrSheMap.get(array[index]) );
	}
	
	public static String getAffirmativePhrase(boolean isSpeech)
	{
		return getAffirmativePhrase(MathUtils.generateRandomNumber(getCorrectResponsePhrases().length-1), isSpeech);
	}

	public static String getAffirmativePhrase(int index, boolean isSpeech) {

		StringBuilder builder = new StringBuilder();
		if (isSpeech)
			builder.append("<say-as interpret-as='interjection'>");
		builder.append(capitalizeFirstLetter(getCorrectResponsePhrases()[index]));
		if (isSpeech)
			builder.append("</say-as><break time='330ms'/>");
		else
			builder.append("!");
		return builder.toString();

	}

	public static String capitalizeFirstLetter(String text) {
		StringBuilder builder = new StringBuilder();
		if (text != null && text.length() > 0) {
			builder.append(text.substring(0, 1).toUpperCase()).append(text.substring(1));
			return builder.toString();
		}
		return null;
	}

}
