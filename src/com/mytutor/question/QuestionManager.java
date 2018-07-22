package com.mytutor.question;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mytutor.dto.QuestionDTO;

public class QuestionManager {
	
	private QuestionManager()
	{
		
	}
	private static final Logger LOG = LoggerFactory.getLogger(QuestionManager.class);


	private static final Map<Integer, String> answerMap;
	
    static
    {
        answerMap = new HashMap<>();
        answerMap.put(1, "A");
        answerMap.put(2, "B");
        answerMap.put(3, "C");
        answerMap.put(4, "D");
        answerMap.put(5, "E");
        answerMap.put(100, "1");
        answerMap.put(101, "2");
        answerMap.put(102, "3");
        answerMap.put(103, "4");
        answerMap.put(104, "5");

    }

    
    private static final Map<Integer, String> letterMap;
    static
    {
    	letterMap = new HashMap<>();
    	letterMap.put(1, "Show me the letter, A");
    	letterMap.put(2, "Show me the letter, B");
    	letterMap.put(3, "Show me the letter, C");
    	letterMap.put(4, "Show me the letter, D");
    	letterMap.put(5, "Show me the letter, E");
    	letterMap.put(6, "Put the blocks <say-as interpret-as='spell-out'>BAT</say-as> to make the word BAT." );
    }
    
    private static final Map<Integer, String> numberMap;
    static
    {
    	numberMap = new HashMap<>();
    	numberMap.put(100, "Show me the number, 1");
    	numberMap.put(101, "Show me the number, 2");
    	numberMap.put(102, "Show me the number, 3");
    	numberMap.put(103, "Show me the number, 4");
    	numberMap.put(104, "Show me the number, 5");

    }
    
    public static QuestionDTO generateQuestion(int type, int id)
    {
    	if(id==0) id = 1;
    	LOG.info("LastQuestion ---->{}",id);
    	String answer = answerMap.get(id);
    
    	String question = null;
    	
    	if (type==0)
    		question = numberMap.get(id);
    	else
    		question= letterMap.get(id);
    	return new QuestionDTO(id, answer, question);
    }
    
 

}
