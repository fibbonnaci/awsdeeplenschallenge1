package com.mytutor.persistence;

import java.util.List;

public class DatabaseManager {

	private QuestionDAO pictureDAO;
	private static DatabaseManager databaseManager;

	private DatabaseManager() {

	}

	public static DatabaseManager getInstance() {
		if (databaseManager == null)
			databaseManager = new DatabaseManager();
		return databaseManager;
	}

	public QuestionDAO getScoreDAO() {
		return pictureDAO;
	}

	public void setScoreDAO(QuestionDAO pictureDAO) {
		this.pictureDAO = pictureDAO;
	}

	public List<Object> getLastIdentifiedObject() {
		List<Object> lastObjects = null;
		try {
			lastObjects = new QuestionDAO().getLastIdentifiedObject();
			System.out.println("Last Identified Object-->" + lastObjects);
		} catch (Exception ex) {
			System.out.println("ERROR: There was an issue getting the last identified object");
			System.out.println(ex);
		}
		return lastObjects;

	}

}
