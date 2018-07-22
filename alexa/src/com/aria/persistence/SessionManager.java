package com.mytutor.persistence;

import com.mytutor.session.UserSession;
import com.mytutor.session.UserSessionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionManager {

	private SessionDAO sessionDAO;

	public SessionManager() {
		super();
		this.sessionDAO = new SessionDAO();
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}

	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}

	public void storeSessionState(UserSession session) {
		try {
			sessionDAO.insertUserSession(session);
		} catch (Exception ex) {
			System.out.println("ERROR: There was an issue writing session data to the database");
			ObjectMapper mapper = new ObjectMapper();
			String json = null;
			try {
				json = mapper.writeValueAsString(session);
			} catch (JsonProcessingException jex) {
				System.out.println("ERROR - " + jex.toString());
			}
			System.out.println(json);
			ex.printStackTrace(System.out);
		}
	}

	public UserSessionData getUsersLastSessionFromToday(String userId) {
		UserSessionData sessionData = sessionDAO.getUserTimeStampForLastSession(userId);
		if (sessionData != null) {
			if (sessionData.getTimeStamp() != null) {
				String lastDateOfUserLogin = sessionData.getTimeStamp().substring(0, 10);

				String todaysDate = DateAndTimeUtils.getTodaysDate();

				if (todaysDate.equals(lastDateOfUserLogin)) {
					// if (sessionData.getIntent().equals("onSessionEnded") &&
					// sessionData.getState().equals("1"))
					return sessionData;
				}
			}

		}
		return null;
	}

	public UserSession getUserLastSessionFromToday(String userId) {
		UserSession sessionData = sessionDAO.getUsersLastSession(userId);
		System.out.println("Getting user's last session..");
		toJSON(sessionData);
		if (sessionData != null) {
			if (sessionData.getTimeStamp() != null) {
				String lastDateOfUserLogin = sessionData.getTimeStamp().substring(0, 10);
				String todaysDate = DateAndTimeUtils.getTodaysDate();
				if (todaysDate.equals(lastDateOfUserLogin)) {
					// if (sessionData.getIntent().equals("onSessionEnded") &&
					// state.intValue()==1)
					return sessionData;
				}
			}

		}
		return null;
	}

	public void toJSON(UserSession session) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(session);
		} catch (JsonProcessingException jex) {
			System.out.println("ERROR - " + jex.toString());
		}
		System.out.println(json.toString());
	}

}
