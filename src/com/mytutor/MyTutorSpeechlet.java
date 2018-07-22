/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.mytutor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.SpeechletV2;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.mytutor.dto.QuestionDTO;
import com.mytutor.persistence.DatabaseManager;
import com.mytutor.persistence.SessionManager;
import com.mytutor.question.QuestionManager;
import com.mytutor.session.SessionHelper;
import com.mytutor.session.UserSession;
import com.mytutor.utils.MessageBuilder;
import com.mytutor.utils.SessionUtils;

/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
class MyTutorSpeechlet implements SpeechletV2 {

	private static final Logger log = LoggerFactory.getLogger(MyTutorSpeechlet.class);

	@Override
	public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
		log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
		log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		return getWelcomeResponse(requestEnvelope.getSession());
	}

	@Override
	public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		IntentRequest request = requestEnvelope.getRequest();
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
				requestEnvelope.getSession().getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		log.info("Intent---> {}", intentName);

		if ("AMAZON.YesIntent".equals(intentName)) {
			return checkAnswer(requestEnvelope.getSession());
		} else if ("DontKnowIntent".equals(intentName)) {
			return getDontKnowAnswerResponse(requestEnvelope.getSession());
		} else if ("GameIntent".equals(intentName)) {
			String gameType = requestEnvelope.getRequest().getIntent().getSlot("game").getValue();
			return changeGameIntent(gameType, requestEnvelope.getSession());
		} else if ("AMAZON.StopIntent".equals(intentName)) {
			return getTellResponse("GoodBye!", "Good Bye.");
		} else if ("AMAZON,RepeatIntent".equals(intentName)) {
			return getRepeatResponse(requestEnvelope.getSession());
		} else {
			log.error("This is unsupported.  Please try something else.");
			return getAskResponse("Error", "This is unsupported.  Please try something else.");
		}
	}

	@Override
	public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
		log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(),
				requestEnvelope.getSession().getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse(Session session) {

		SessionManager sessionManager = new SessionManager();
		UserSession sessionData = sessionManager.getUserLastSessionFromToday(session.getUser().getUserId());

		log.info("sessionId={} Retrieved User Session", session.getSessionId());

		sessionManager.toJSON(sessionData);

		if (sessionData != null) {

			session = SessionUtils.constructSessionFromUsersLastSession(sessionData, session);
		}
		int questionID = 1;
		int gameType = 0;
		if (session.getAttribute("id") != null) {
			questionID = SessionUtils.getNumericValueFromSession(session, "id");
			gameType = SessionUtils.getNumericValueFromSession(session, "gameType");

			QuestionDTO question = QuestionManager.generateQuestion(gameType, questionID);
			storeQuestionInSession(session, question);
			String speechText = question.getQuestion()
					+ "<audio src='https://s3.amazonaws.com/********/blank_sound.mp3'/>";
			String repromptText = "Are you ready? <audio src='https://s3.amazonaws.com/************/blank_sound.mp3'/>";
			return getAskResponse("Question", speechText, repromptText);
		} else {
			String speechText = "Which game do you want to play?  Alphabets, or, Numbers ?";
			String repromptText = "Alphabets, or Numbers?";
			return getAskResponse("Question", speechText, repromptText);
		}
	}

	private SpeechletResponse changeGameIntent(String gameType, Session session) {
		int questionID = 1;
		int game = 1;

		if (gameType.equalsIgnoreCase("NUMBERS")) {
			questionID = 100;
			game = 0;
		}
		session.setAttribute("gameType", game);

		QuestionDTO question = QuestionManager.generateQuestion(game, questionID);

		storeQuestionInSession(session, question);
		String speechText = "OK!" + question.getQuestion()
				+ "<audio src='https://s3.amazonaws.com/shan-alexa-skill-sounds/blank_sound.mp3'/>";
		String repromptText = "Are you ready? <audio src='https://s3.amazonaws.com/shan-alexa-skill-sounds/blank_sound.mp3'/>";
		return getAskResponse("Question", speechText, repromptText);
	}

	private SpeechletResponse getRepeatResponse(Session session) {
		String question = (String) session.getAttribute("question");
		return getAskResponse("Question", question);
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse checkAnswer(Session session) {
		List<Object> answerFromUser = DatabaseManager.getInstance().getLastIdentifiedObject();

		String correctAnswer = (String) session.getAttribute("answer");
		String question = (String) session.getAttribute("question");

		String speechText = "";
		String cardText = "";
		String repromptText = "Are you ready? <audio src='https://s3.amazonaws.com/shan-alexa-skill-sounds/blank_sound.mp3'/>";

		if (answerFromUser == null) {
			speechText = cardText = " Sorry, I could not capture the image. Please try again. " + question;
		} else {
			List<Object> answerFromUserTransformed = new ArrayList<>();
			for (Object word : answerFromUser) {

				if (word != null) {
					String transformedWord = ((String) word).replaceAll("\\s+", "");

					answerFromUserTransformed.add(transformedWord.toLowerCase());

				}
				log.info("Word={}", word);
			}

			if (correctAnswer != null)

			{
				log.info("Correct Answer--->{}", correctAnswer);

				correctAnswer = correctAnswer.toLowerCase();
			}

			if (!answerFromUserTransformed.contains(correctAnswer)) {

				speechText = "Good try, but that is not the right one. Please try again. <audio src='https://s3.amazonaws.com/shan-alexa-skill-sounds/blank_sound.mp3'/>";
				cardText = "Good try, but that is not the right one. Please try again.";
			} else if (answerFromUserTransformed.contains(correctAnswer)) {
				speechText = MessageBuilder.getAffirmativePhrase(true) + MessageBuilder.CONGRATULATIONS_AUDIO_FILE
						+ " You got it! Let's try another one. ";
				cardText = MessageBuilder.getAffirmativePhrase(false) + " You got it! Let's try another one. ";
				int questionID = 1;
				int gameType = 0;
				if (session.getAttribute("id") != null) {
					questionID = SessionUtils.getNumericValueFromSession(session, "id");
					gameType = SessionUtils.getNumericValueFromSession(session, "gameType");
				}

				QuestionDTO newQuestion = QuestionManager.generateQuestion(gameType, questionID + 1);
				storeQuestionInSession(session, newQuestion);
				speechText = speechText + newQuestion.getQuestion()
						+ "<audio src='https://s3.amazonaws.com/shan-alexa-skill-sounds/blank_sound.mp3'/>";
				cardText = cardText + newQuestion.getQuestion();
			}
		}

		return getAskResponse("Question", speechText, cardText, repromptText);
	}

	private SpeechletResponse getDontKnowAnswerResponse(Session session) {
		String answer = (String) session.getAttribute("answer");
		String speechText = "The correct answer is " + answer + ". ";
		int questionID = 1;
		int gameType = 1;
		if (session.getAttribute("id") != null) {
			questionID = SessionUtils.getNumericValueFromSession(session, "id");
			gameType = SessionUtils.getNumericValueFromSession(session, "gameType");
		}

		QuestionDTO question = QuestionManager.generateQuestion(gameType, questionID);
		storeQuestionInSession(session, question);
		speechText = speechText + question.getQuestion();
		String repromptText = "Are you ready?";
		return getAskResponse("Question", speechText, repromptText);
	}

	/**
	 * Creates a {@code SpeechletResponse} for the hello intent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */

	/**
	 * Helper method that creates a card object.
	 * 
	 * @param title
	 *            title of the card
	 * @param content
	 *            body of the card
	 * @return SimpleCard the display card to be sent along with the voice
	 *         response.
	 */
	private SimpleCard getSimpleCard(String title, String content) {
		SimpleCard card = new SimpleCard();
		card.setTitle(title);
		card.setContent(content);

		return card;
	}

	/**
	 * Helper method for retrieving an OutputSpeech object when given a string
	 * of TTS.
	 * 
	 * @param speechText
	 *            the text that should be spoken out to the user.
	 * @return an instance of SpeechOutput.
	 */
	private PlainTextOutputSpeech getPlainTextOutputSpeech(String speechText) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return speech;
	}

	private SsmlOutputSpeech getSSMLTextOutputSpeech(String speechText) {
		SsmlOutputSpeech speech = new SsmlOutputSpeech();
		speech.setSsml("<speak>" + speechText + "</speak>");

		return speech;
	}

	/**
	 * Helper method that returns a reprompt object. This is used in Ask
	 * responses where you want the user to be able to respond to your speech.
	 * 
	 * @param outputSpeech
	 *            The OutputSpeech object that will be said once and repeated if
	 *            necessary.
	 * @return Reprompt instance.
	 */
	private Reprompt getReprompt(OutputSpeech outputSpeech) {
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(outputSpeech);

		return reprompt;
	}

	/**
	 * Helper method for retrieving an Ask response with a simple card and
	 * reprompt included.
	 * 
	 * @param cardTitle
	 *            Title of the card that you want displayed.
	 * @param speechText
	 *            speech text that will be spoken to the user.
	 * @return the resulting card and speech text.
	 */
	private SpeechletResponse getAskResponse(String cardTitle, String speechText) {
		SimpleCard card = getSimpleCard(cardTitle, speechText);
		PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
		Reprompt reprompt = getReprompt(speech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	private SpeechletResponse getTellResponse(String cardTitle, String speechText) {
		SimpleCard card = getSimpleCard(cardTitle, speechText);
		PlainTextOutputSpeech speech = getPlainTextOutputSpeech(speechText);
		return SpeechletResponse.newTellResponse(speech, card);
	}

	private SpeechletResponse getAskResponse(String cardTitle, String speechText, String cardText,
			String repromptText) {
		SimpleCard card = getSimpleCard(cardTitle, cardText);
		SsmlOutputSpeech speech = getSSMLTextOutputSpeech(speechText);
		SsmlOutputSpeech repromptSpeech = getSSMLTextOutputSpeech(repromptText);
		Reprompt reprompt = getReprompt(repromptSpeech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}

	private SpeechletResponse getAskResponse(String cardTitle, String speechText, String repromptText) {
		return getAskResponse(cardTitle, speechText, speechText, repromptText);
	}

	private void storeQuestionInSession(Session session, QuestionDTO question) {
		if (question != null) {
			session.setAttribute("question", question.getQuestion());
			session.setAttribute("id", question.getId());
			session.setAttribute("answer", question.getAnswer());
		}
		SessionHelper.saveSession(session.getUser().getUserId(), "Question", session);

	}
}
