package com.mytutor.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

public class QuestionDAO {

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
				.withValueMap(new ValueMap()
						.withString(":v_userId", "test")
						.withLong(":v_timeStamp", currentTimeInMillis-20000));

		ItemCollection<QueryOutcome> items =null;
		try {
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
		List<Object> result= new ArrayList<Object>();
		while (iterator.hasNext())

		{
			Item item = iterator.next();
			if(item!=null)
			{
				result=item.getList("texts");
			}
		}
		return result;

	}

	
}
