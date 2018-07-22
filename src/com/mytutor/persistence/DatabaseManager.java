package com.mytutor.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);


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
			LOG.info("Last Identified Object-->{}" , lastObjects);
		} catch (Exception ex) {
			LOG.error("ERROR: There was an issue getting the last identified object");
			LOG.error(ex.getMessage());
		}
		return lastObjects;

	}

}
