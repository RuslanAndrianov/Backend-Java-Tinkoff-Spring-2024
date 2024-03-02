package edu.java.model.response;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
}
