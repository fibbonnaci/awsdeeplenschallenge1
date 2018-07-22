package com.mytutor.dto;

public class QuestionDTO {
	
	private int id;
	private String answer;
	private String question;
	
	public QuestionDTO()
	{
		
	}
	
	public QuestionDTO(int id, String answer, String question) {
		super();
		this.id = id;
		this.answer = answer;
		this.question = question;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	

}
