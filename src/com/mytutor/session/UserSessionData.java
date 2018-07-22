package com.mytutor.session;

public class UserSessionData {

	private String intent;
	private String timeStamp;
	private String answerFromUser;
	private String answerFromSession;
	private String questionType;
	private String question;
	private String userId;
	private String sessionId;
	private String state;
	private String correctAnswers;
	private String wrongAnswers;
	private String difficultyMode;

	public UserSessionData() {

	}

	public UserSessionData(String intent, String answerFromUser, String answerFromSession, String questionType,
			String question, String userId, String sessionId, String state, String correctAnswers, String wrongAnswers,
			String difficultyMode, String timeStamp) {
		super();
		this.intent = intent;
		this.answerFromUser = answerFromUser;
		this.answerFromSession = answerFromSession;
		this.questionType = questionType;
		this.question = question;
		this.userId = userId;
		this.sessionId = sessionId;
		this.state = state;
		this.correctAnswers = correctAnswers;
		this.wrongAnswers = wrongAnswers;
		this.difficultyMode = difficultyMode;
		this.timeStamp = timeStamp;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getAnswerFromUser() {
		return answerFromUser;
	}

	public void setAnswerFromUser(String answerFromUser) {
		this.answerFromUser = answerFromUser;
	}

	public String getAnswerFromSession() {
		return answerFromSession;
	}

	public void setAnswerFromSession(String answerFromSession) {
		this.answerFromSession = answerFromSession;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(String correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	public String getWrongAnswers() {
		return wrongAnswers;
	}

	public void setWrongAnswers(String wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	public String getDifficultyMode() {
		return difficultyMode;
	}

	public void setDifficultyMode(String difficultyMode) {
		this.difficultyMode = difficultyMode;
	}

}
