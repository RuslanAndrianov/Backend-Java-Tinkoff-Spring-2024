package edu.java.clients.StackOverflow;

public interface StackOverflowClient {

    QuestionResponse fetchQuestion(Long questionId);
}
