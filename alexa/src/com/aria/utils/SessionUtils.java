package com.mytutor.utils;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

import com.amazon.speech.speechlet.Session;
import com.mytutor.session.UserSession;

public class SessionUtils {
	
	public static String getNonEmptyString(Session session, String key)
	{
		String value = (String) session.getAttribute(key);
		
		if(value==null || value.length()==0)
			return "null";
		else
			return value;
		
	}
	
	public static int getIntValue(Session session, String key)
	{
		
		int value = 0;
		
		try
		{
			value =(session.getAttribute(key)!=null) ? (int)(session.getAttribute(key)):0;
		}
		catch(Exception nex)
		{
			System.out.println(nex);
		}
		
		return value;
	}
	
	public static int getNumericValueFromSession(Session session,String name)
	{
		if(session.getAttribute(name)==null)
			return 0;
		if(session.getAttribute(name) instanceof BigDecimal)
			return ((BigDecimal)session.getAttribute(name)).intValue();
		return (int)session.getAttribute(name);
	}
	
	public static void printSessionVariables(Session session)
	{
		System.out.println("*******************Session variables*******************");
		for(Iterator keySet=session.getAttributes().keySet().iterator();keySet.hasNext();)
		{
			String key = (String)keySet.next();
			System.out.println(key+"="+session.getAttribute(key));
		}
		System.out.println("********************************************************");
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
