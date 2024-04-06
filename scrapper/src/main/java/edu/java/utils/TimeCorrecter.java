package edu.java.utils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TimeCorrecter {

    private TimeCorrecter() { }

    public static OffsetDateTime getCorrectedTime(OffsetDateTime timeForCorrection, int offsetSeconds) {
        return OffsetDateTime.ofInstant(
            Instant.ofEpochSecond(timeForCorrection.toEpochSecond() - offsetSeconds),
            ZoneOffset.UTC
        );
    }
}
