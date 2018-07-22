package com.mytutor.session;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.Session;
import com.mytutor.persistence.SessionManager;
import com.mytutor.utils.DateAndTimeUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(SessionHelper.class);
	private static SessionManager sessionManager = new SessionManager();
	
	
	public static LinkedHashMap getQuestionMap(Session session)
	{

		String sessionMap = (String) session.getAttribute("question");
		
		System.out.println("Session Map = "+sessionMap);
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Map<String, Object> jsonMap = objectMapper.readValue(sessionMap,
			    new TypeReference<Map<String,Object>>(){});
			return (LinkedHashMap)jsonMap;
		} catch (JsonParseException e) {

			System.out.println("Error parsing Json");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			System.out.println("Error mapping Json");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Error parsing Json");
			e.printStackTrace();
		}
		return null;

	}
	
	public static void saveSession(String userId, String intent, Session session) {

		// Store the state in the Database
		String todaysDateAndTime = DateAndTimeUtils.getTodaysDateAndTime();

		UserSession userSession = new UserSession(userId, intent, todaysDateAndTime, session.getAttributes());
		sessionManager.storeSessionState(userSession);

	}

}
