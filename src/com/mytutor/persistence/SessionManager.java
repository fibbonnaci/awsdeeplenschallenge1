package com.mytutor.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytutor.session.UserSession;

public class SessionManager {
	private static final Logger LOG = LoggerFactory.getLogger(SessionManager.class);

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
			LOG.error("ERROR: There was an issue writing session data to the database");
			ObjectMapper mapper = new ObjectMapper();
			String json = null;
			try {
				json = mapper.writeValueAsString(session);
			} catch (JsonProcessingException jex) {
				LOG.error("ERROR - {} ", jex);
			}
			LOG.error(json);
			LOG.error(ex.getMessage());
		}
	}

	

	public UserSession getUserLastSessionFromToday(String userId) {
		UserSession sessionData = sessionDAO.getUsersLastSession(userId);
		LOG.info("Getting user's last session..");
		toJSON(sessionData);
		if (sessionData != null && sessionData.getTimeStamp() != null) {

			String lastDateOfUserLogin = sessionData.getTimeStamp().substring(0, 10);
			String todaysDate = DateAndTimeUtils.getTodaysDate();
			if (todaysDate.equals(lastDateOfUserLogin)) {
				return sessionData;
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
			LOG.error("ERROR - {} ", jex);
		}
		LOG.info(json);
	}

}
