package edu.java.scrapper.clients.StackOverflow;

public interface StackOverflowClient {

    StackOverflowItemsResponse fetchQuestion(Long questionId);
}
