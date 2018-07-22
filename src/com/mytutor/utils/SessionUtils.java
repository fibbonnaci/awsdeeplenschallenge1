package com.mytutor.utils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.Session;
import com.mytutor.session.UserSession;

public class SessionUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(SessionUtils.class);
	
	private SessionUtils()
	{
		
	}

	
	public static int getNumericValueFromSession(Session session,String name)
	{
		if(session.getAttribute(name)==null)
			return 0;
		if(session.getAttribute(name) instanceof BigDecimal)
			return ((BigDecimal)session.getAttribute(name)).intValue();
		return (int)session.getAttribute(name);
	}
	
	
	public static Session constructSessionFromUsersLastSession(UserSession sessionData, Session session) {
		Map<String, Object> attributes = sessionData.getSessionAttributes();
		for (Iterator<String> iterator = attributes.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			session.setAttribute(key, attributes.get(key));
		}
		return session;
	}
}
