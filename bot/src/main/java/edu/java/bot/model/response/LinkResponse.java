package edu.java.bot.model.response;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) { }
