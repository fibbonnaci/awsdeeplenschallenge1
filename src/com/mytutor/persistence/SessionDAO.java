package com.mytutor.persistence;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mytutor.session.UserSession;

class SessionDAO {

	private static final Logger LOG = LoggerFactory.getLogger(SessionDAO.class);

	private Table table;

	public SessionDAO() {
		AmazonDynamoDBAsync client = AmazonDynamoDBAsyncClientBuilder.standard().build();

		DynamoDB dynamoDB = new DynamoDB(client);
		String environment = System.getenv("ENVIRONMENT");
		String tableName = System.getenv("SESSIONS_TABLE");
		if (environment != null)
			tableName = environment + "_" + tableName;
		table = dynamoDB.getTable(tableName);
	}


	void insertUserSession(UserSession session) {

		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(session);
		} catch (JsonProcessingException jex) {
			LOG.error("ERROR - {}", jex);
		}
		LOG.error(json);

		try {
			Item item = new Item()
					.withPrimaryKey("userId", session.getUserId(), "timeStampRecord", session.getTimeStamp())
					.withString("intent", session.getIntent()).withMap("attributes", session.getSessionAttributes());
			table.putItem(item);
		} catch (AmazonServiceException ase)

		{
			LOG.error("Could not complete operation");
			LOG.error("Error Message:{}", ase.getMessage());
			LOG.error("HTTP Status:  {}   ", ase.getStatusCode());
			LOG.error("AWS Error Code: {}", ase.getErrorCode());
			LOG.error("Error Type:   {}   ", ase.getErrorType());
			LOG.error("Request ID:   {}   ", ase.getRequestId());

		} catch (AmazonClientException ace)

		{
			LOG.error("Internal error occured communicating with DynamoDB");
			LOG.error("Error Message: {}", ace.getMessage());

		}

	}

	
	UserSession getUsersLastSession(String userId) {
		ItemCollection<QueryOutcome> items = null;

		try {
			QuerySpec spec = new QuerySpec().withKeyConditionExpression("userId = :v_id")
					.withValueMap(new ValueMap().withString(":v_id", userId)).withScanIndexForward(false);

			items = table.query(spec);
		} catch (AmazonServiceException ase)

		{
			LOG.error("Could not complete operation");
			LOG.error("Error Message:{}", ase.getMessage());
			LOG.error("HTTP Status:    {}", ase.getStatusCode());
			LOG.error("AWS Error Code:{}", ase.getErrorCode());
			LOG.error("Error Type:     {}", ase.getErrorType());
			LOG.error("Request ID:     {}", ase.getRequestId());

		} catch (AmazonClientException ace)

		{
			LOG.error("Internal error occured communicating with DynamoDB");
			LOG.error("Error Message: {}", ace.getMessage());

		}

		Iterator<Item> iterator = null;
		Item item = null;

		UserSession sessionData = null;
		if (items != null) {
			iterator = items.iterator();

			while (iterator.hasNext()) {
				item = iterator.next();
				String timeStamp = (String) item.get("timeStampRecord");
				String intent = (String) item.get("intent");
				Map<String, Object> attributes = (Map<String, Object>) item.get("attributes");
				sessionData = new UserSession(userId, intent, timeStamp, attributes);
				break;
			}
		}
		return sessionData;
	}

}
