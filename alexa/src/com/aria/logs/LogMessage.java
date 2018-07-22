package com.mytutor.logs;

////@DynamoDBTable(tableName="QuestionsLog")
public class LogMessage {
	private String severity;
	private String intent;
	private String timeStamp;
	private String answerFromUser;
	private String answerFromSession;
	private String questionType;
	private String question;
	private String userId;
	private String sessionId;
	private String correctAnswers;
	private String wrongAnswers;
	private String mode;
	private String state;
	
	public LogMessage(String severity, String intent, String timeStamp, String answerFromUser, String answerFromSession,
			String questionType, String question, String userId, String sessionId, String correctAnswers, String wrongAnswers, String mode, String state) {
		super();
		this.severity = severity;
		this.intent = intent;
		this.timeStamp = timeStamp;
		this.answerFromUser = answerFromUser;
		this.answerFromSession = answerFromSession;
		this.questionType = questionType;
		this.question = question;
		this.userId = userId;
		this.sessionId = sessionId;
		this.correctAnswers = correctAnswers;
		this.wrongAnswers = wrongAnswers;
		this.mode=mode;
		this.state=state;
	}


	//@DynamoDBAttribute(attributeName = "severity")
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	//@DynamoDBAttribute(attributeName = "intent")
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	
	//@DynamoDBAttribute(attributeName = "timestamp")
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	//@DynamoDBAttribute(attributeName = "answerFromUser")
	public String getAnswerFromUser() {
		return answerFromUser;
	}
	public void setAnswerFromUser(String answerFromUser) {
		this.answerFromUser = answerFromUser;
	}
	
	//@DynamoDBAttribute(attributeName = "correctAnswer")
	public String getAnswerFromSession() {
		return answerFromSession;
	}
	public void setAnswerFromSession(String answerFromSession) {
		this.answerFromSession = answerFromSession;
	}
	
	//@DynamoDBAttribute(attributeName = "questionType")
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	//@DynamoDBAttribute(attributeName = "questionText")
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	//@DynamoDBAttribute(attributeName = "userID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	//@DynamoDBAttribute(attributeName = "sessionID")
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}
	
	
	

}
