package com.mytutor.persistence;

import java.util.Iterator;
import java.util.Map;

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
import com.mytutor.session.UserSession;
import com.mytutor.session.UserSessionData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionDAO {

	private Table table;

	public SessionDAO() {
		AmazonDynamoDBAsync client = AmazonDynamoDBAsyncClientBuilder.standard().build();
		// AmazonDynamoDB client =
		// AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
		// new AwsClientBuilder.EndpointConfiguration("http://localhost:8000",
		// "us-east-1"))
		// .build();
		DynamoDB dynamoDB = new DynamoDB(client);
		String environment = System.getenv("ENVIRONMENT");
		String tableName = System.getenv("SESSIONS_TABLE");
		// String tableName = "123Math_Sessions";
		if (environment != null)
			tableName = environment + "_" + tableName;
		table = dynamoDB.getTable(tableName);
	}

	public void insertSessionData(UserSessionData session) {

		String todaysDateAndTime = DateAndTimeUtils.getTodaysDateAndTime();
		try {
			Item item = new Item().withPrimaryKey("userId", session.getUserId(), "timeStampRecord", todaysDateAndTime)
					.withString("sessionId", session.getSessionId()).withString("intent", session.getIntent())
					.withString("answerFromUser", session.getAnswerFromUser())
					.withString("answerFromSession", session.getAnswerFromSession())
					.withString("questionId", session.getQuestionType()).withString("question", session.getQuestion());
			table.putItem(item);
		} catch (AmazonServiceException ase)

		{
			System.out.println("Could not complete operation");
			System.out.println("Error Message:  " + ase.getMessage());
			System.out.println("HTTP Status:    " + ase.getStatusCode());
			System.out.println("AWS Error Code: " + ase.getErrorCode());
			System.out.println("Error Type:     " + ase.getErrorType());
			System.out.println("Request ID:     " + ase.getRequestId());
			ase.printStackTrace();

		} catch (AmazonClientException ace)

		{
			System.out.println("Internal error occured communicating with DynamoDB");
			System.out.println("Error Message:  " + ace.getMessage());
			ace.printStackTrace();
		}
	}

	public void insertUserSession(UserSession session) {
		// System.out.println("UserId="+session.getUserId());
		// System.out.println("TimeStamp="+session.getTimeStamp());
		// System.out.println("Intent="+session.getIntent());
		// System.out.println("Attributes="+session.getSessionAttributes());
		/*
		 * System.out.println("Inserting Data to Dynamo ***************"); for
		 * (Iterator<String> iterator =
		 * session.getSessionAttributes().keySet().iterator();
		 * iterator.hasNext();) { String key1 = iterator.next();
		 * System.out.println(
		 * key1+"="+session.getSessionAttributes().get(key1)+" "
		 * +session.getSessionAttributes().get(key1).getClass()); }
		 */

		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(session);
		} catch (JsonProcessingException jex) {
			System.out.println("ERROR - " + jex.toString());
		}
		System.out.println(json.toString());

		try {
			Item item = new Item()
					.withPrimaryKey("userId", session.getUserId(), "timeStampRecord", session.getTimeStamp())
					.withString("intent", session.getIntent()).withMap("attributes", session.getSessionAttributes());
			table.putItem(item);
		} catch (AmazonServiceException ase)

		{
			System.out.println("Could not complete operation");
			System.out.println("Error Message:  " + ase.getMessage());
			System.out.println("HTTP Status:    " + ase.getStatusCode());
			System.out.println("AWS Error Code: " + ase.getErrorCode());
			System.out.println("Error Type:     " + ase.getErrorType());
			System.out.println("Request ID:     " + ase.getRequestId());
			ase.printStackTrace();

		} catch (AmazonClientException ace)

		{
			System.out.println("Internal error occured communicating with DynamoDB");
			System.out.println("Error Message:  " + ace.getMessage());
			ace.printStackTrace();
		}

	}

	public UserSessionData getUserTimeStampForLastSession(String userId) {
		ItemCollection<QueryOutcome> items = null;

		try {

			QuerySpec spec = new QuerySpec().withKeyConditionExpression("userId = :v_id")
					.withValueMap(new ValueMap().withString(":v_id", userId)).withScanIndexForward(false);

			items = table.query(spec);
		} catch (AmazonServiceException ase)

		{
			System.out.println("Could not complete operation");
			System.out.println("Error Message:  " + ase.getMessage());
			System.out.println("HTTP Status:    " + ase.getStatusCode());
			System.out.println("AWS Error Code: " + ase.getErrorCode());
			System.out.println("Error Type:     " + ase.getErrorType());
			System.out.println("Request ID:     " + ase.getRequestId());
			ase.printStackTrace();

		} catch (AmazonClientException ace)

		{
			System.out.println("Internal error occured communicating with DynamoDB");
			System.out.println("Error Message:  " + ace.getMessage());
			ace.printStackTrace();
		}

		Iterator<Item> iterator = items.iterator();
		Item item = null;
		// String timeStamp=null;

		UserSessionData sessionData = null;
		while (iterator.hasNext()) {
			item = iterator.next();
			String timeStamp = (String) item.get("timeStampRecord");
			String sessionId = (String) item.get("sessionId");
			String intent = (String) item.get("intent");
			String answerFromUser = (String) item.get("answerFromUser");
			String answerFromSession = (String) item.get("answerFromSession");
			String questionId = (String) item.get("questionId");
			String question = (String) item.get("timeStampRecord");
			String gameState = (String) item.get("gameState");
			String correctAnswers = (String) item.get("correctAnswers");
			String wrongAnswers = (String) item.get("wrongAnswers");
			String mode = (String) item.get("mode");
			sessionData = new UserSessionData(intent, answerFromUser, answerFromSession, questionId, question, userId,
					sessionId, gameState, correctAnswers, wrongAnswers, mode, timeStamp);
			break;
		}
		return sessionData;
	}

	public UserSession getUsersLastSession(String userId) {
		ItemCollection<QueryOutcome> items = null;

		try {
			QuerySpec spec = new QuerySpec().withKeyConditionExpression("userId = :v_id")
					.withValueMap(new ValueMap().withString(":v_id", userId)).withScanIndexForward(false);

			items = table.query(spec);
		} catch (AmazonServiceException ase)

		{
			System.out.println("Could not complete operation");
			System.out.println("Error Message:  " + ase.getMessage());
			System.out.println("HTTP Status:    " + ase.getStatusCode());
			System.out.println("AWS Error Code: " + ase.getErrorCode());
			System.out.println("Error Type:     " + ase.getErrorType());
			System.out.println("Request ID:     " + ase.getRequestId());
			ase.printStackTrace();

		} catch (AmazonClientException ace)

		{
			System.out.println("Internal error occured communicating with DynamoDB");
			System.out.println("Error Message:  " + ace.getMessage());
			ace.printStackTrace();
		}

		Iterator<Item> iterator = items.iterator();
		Item item = null;
		// String timeStamp=null;

		UserSession sessionData = null;
		while (iterator.hasNext()) {
			item = iterator.next();
			String timeStamp = (String) item.get("timeStampRecord");
			String intent = (String) item.get("intent");
			Map<String, Object> attributes = (Map<String, Object>) item.get("attributes");
			sessionData = new UserSession(userId, intent, timeStamp, attributes);
			break;
		}
		return sessionData;
	}

}
