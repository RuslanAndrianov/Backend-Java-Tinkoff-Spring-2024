package edu.java.clients.StackOverflow;

public interface StackOverflowClient {

    StackOverflowItemsResponse fetchQuestion(Long questionId);
}
