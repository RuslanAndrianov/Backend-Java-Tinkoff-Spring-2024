package edu.java.model.response;

import java.net.URI;
import org.springframework.stereotype.Component;

@Component
public record LinkResponse(
    Long id,
    URI url
) { }
