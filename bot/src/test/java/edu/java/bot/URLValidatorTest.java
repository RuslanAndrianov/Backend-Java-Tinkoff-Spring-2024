package edu.java.bot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static edu.utils.URLValidator.isValidURL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class URLValidatorTest {
    String LINK1 = "https://github.com";
    String LINK2 = "invalid_url";

    @Test
    @DisplayName("Правильная и неправильная ссылки")
    void validAndInvalidURL() {
        assertTrue(isValidURL(LINK1));
        assertFalse(isValidURL(LINK2));
    }
}
