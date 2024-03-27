package edu.shared_dto.response_dto;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
}
