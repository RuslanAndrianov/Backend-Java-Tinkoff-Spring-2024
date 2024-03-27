package edu.shared_dto.response_dto;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) { }
