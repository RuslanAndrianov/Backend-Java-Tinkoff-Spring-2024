package edu.java.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.utils.URLValidator.isValidGitHubURL;
import static edu.utils.URLValidator.isValidStackOverflowURL;
import static edu.utils.URLValidator.isValidURL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class URLValidatorTest {
    String LINK1 = "https://github.com";
    String LINK2 = "invalid_url";
    String LINK3 = "https://github.com/RuslanAndrianov/Backend-Java-Tinkoff-Spring-2024";
    String LINK4 = "https://stackoverflow.com/questions/78395176/how-do-javascript-decide-how-to-print-a-class-name";

    @Test
    @DisplayName("Тест основного валидатора")
    void validURL() {
        assertTrue(isValidURL(LINK1));
        assertFalse(isValidURL(LINK2));
        assertTrue(isValidURL(LINK3));
        assertTrue(isValidURL(LINK4));
    }

    @Test
    @DisplayName("Проверка regexp для GitHub")
    void githubTest() {
        assertFalse(isValidGitHubURL(LINK1));
        assertFalse(isValidGitHubURL(LINK2));
        assertTrue(isValidGitHubURL(LINK3));
        assertFalse(isValidGitHubURL(LINK4));
    }

    @Test
    @DisplayName("Проверка regexp для Stack Overflow")
    void stackoverflowTest() {
        assertFalse(isValidStackOverflowURL(LINK1));
        assertFalse(isValidStackOverflowURL(LINK2));
        assertFalse(isValidStackOverflowURL(LINK3));
        assertTrue(isValidStackOverflowURL(LINK4));
    }
}
