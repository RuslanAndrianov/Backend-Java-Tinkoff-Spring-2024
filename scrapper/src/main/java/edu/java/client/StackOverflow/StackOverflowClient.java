package edu.java.client.StackOverflow;

public interface StackOverflowClient {

    QuestionResponse fetchQuestion(long questionId, String order, String sort);
}
