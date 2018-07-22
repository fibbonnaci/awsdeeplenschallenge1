package com.mytutor.session;

import java.util.Map;

public class UserSession {

	private String userId;
	private String intent;
	private String timeStamp;
	private Map<String, Object> sessionAttributes;

	public UserSession(String userId, String intent, String timeStamp, Map<String, Object> sessionAttributes) {
		super();
		this.userId = userId;
		this.timeStamp = timeStamp;
		this.intent = intent;
		this.sessionAttributes = sessionAttributes;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Map<String, Object> getSessionAttributes() {
		return sessionAttributes;
	}

	public void setSessionAttributes(Map<String, Object> sessionAttributes) {
		this.sessionAttributes = sessionAttributes;
	}

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}
	
	

}
