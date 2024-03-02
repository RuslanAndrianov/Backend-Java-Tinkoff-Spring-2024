package edu.java.model.response;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public record APIErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stacktrace
) { }
