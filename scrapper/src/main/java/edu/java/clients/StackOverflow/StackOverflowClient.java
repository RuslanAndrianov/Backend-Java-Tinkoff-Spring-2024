package edu.java.clients.StackOverflow;

public interface StackOverflowClient {

    StackOverflowResponse fetchQuestion(Long questionId);
}
