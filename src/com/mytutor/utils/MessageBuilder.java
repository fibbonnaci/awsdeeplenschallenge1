package com.mytutor.utils;

public class MessageBuilder {
	
	private MessageBuilder()
	{
		
	}

	public static final String  CONGRATULATIONS_AUDIO_FILE = "<audio src=\"https://s3.amazonaws.com/123math/audio_files/alexa_happykids.mp3\" />";

	private static String[] correctResponsePhrases;

	private static String[] getCorrectResponsePhrases() {
		if (correctResponsePhrases == null) {

			correctResponsePhrases = new String[] { "bazinga", "booya", "bravo", "cha ching", "yippee", "hurray",
					"bingo", "ooh la la", "ta da", "wahoo" };

		}
		return correctResponsePhrases;

	}

	
	public static String getAffirmativePhrase(boolean isSpeech)
	{
		return getAffirmativePhrase(MathUtils.generateRandomNumber(getCorrectResponsePhrases().length-1), isSpeech);
	}

	private static String getAffirmativePhrase(int index, boolean isSpeech) {

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

	private static String capitalizeFirstLetter(String text) {
		StringBuilder builder = new StringBuilder();
		if (text != null && text.length() > 0) {
			builder.append(text.substring(0, 1).toUpperCase()).append(text.substring(1));
			return builder.toString();
		}
		return null;
	}

}
