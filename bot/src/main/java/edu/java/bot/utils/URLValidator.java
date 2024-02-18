package edu.java.bot.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("RegexpSinglelineJava")
public class URLValidator {

    private URLValidator() {}

    public static boolean isValidURL(String text) {
        try {
            URL url = new URL(text);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            int responseCode = huc.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }
}
