package com.mytutor.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

class QuestionDAO {
	private static final Logger LOG = LoggerFactory.getLogger(QuestionDAO.class);


	private Table table;

	public QuestionDAO() {
		AmazonDynamoDBAsync client = AmazonDynamoDBAsyncClientBuilder.standard().build();

		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "text-detection-deeplens";
		table = dynamoDB.getTable(tableName);
	}

	public List<Object> getLastIdentifiedObject() {

		long currentTimeInMillis = System.currentTimeMillis();

		QuerySpec spec = new QuerySpec().withProjectionExpression("texts")
				.withKeyConditionExpression("userId = :v_userId and timeStampRecord >=:v_timeStamp")
				.withValueMap(new ValueMap().withString(":v_userId", "test").withLong(":v_timeStamp",
						currentTimeInMillis - 20000));

		ItemCollection<QueryOutcome> items = null;
		try {
			items = table.query(spec);

		} catch (AmazonServiceException ase)

		{
			LOG.error("Could not complete operation");
			LOG.error("Error Message: {} ", ase.getMessage());
			LOG.error("HTTP Status:    {} ", ase.getStatusCode());
			LOG.error("AWS Error Code: {} ", ase.getErrorCode());
			LOG.error("Error Type:      {} ", ase.getErrorType());
			LOG.error("Request ID:     {} ", ase.getRequestId());

		} catch (AmazonClientException ace)

		{
			LOG.error("Internal error occured communicating with DynamoDB");
			LOG.error("Error Message:  {} ",ace.getMessage());
		}

		Iterator<Item> iterator = null;
		List<Object> result = new ArrayList<>();
		if (items != null)
		{
			iterator = items.iterator();
			while (iterator.hasNext())

			{
				Item item = iterator.next();
				if (item != null) {
					result = item.getList("texts");
				}
			}
		}
		return result;

	}

}
