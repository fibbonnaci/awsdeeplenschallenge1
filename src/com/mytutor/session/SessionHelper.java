package com.mytutor.session;

import com.amazon.speech.speechlet.Session;
import com.mytutor.persistence.DateAndTimeUtils;
import com.mytutor.persistence.SessionManager;

public class SessionHelper {
	
	private SessionHelper()
	{
		
	}
	
	private static SessionManager sessionManager = new SessionManager();
	
	
	 
	public static void saveSession(String userId, String intent, Session session) {

		// Store the state in the Database
		String todaysDateAndTime = DateAndTimeUtils.getTodaysDateAndTime();

		UserSession userSession = new UserSession(userId, intent, todaysDateAndTime, session.getAttributes());
		sessionManager.storeSessionState(userSession);

	}

}
