package edu.java.model.response;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
}
